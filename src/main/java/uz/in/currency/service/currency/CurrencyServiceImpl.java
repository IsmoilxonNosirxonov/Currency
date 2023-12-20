package uz.in.currency.service.currency;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.entity.Currency;
import uz.in.currency.domain.dto.CurrencyCreateDto;
import uz.in.currency.domain.exception.CurrencyNotSaveException;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.feign.CurrencyFeignClient;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.mapper.MyMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{

    private final CurrencyRepository currencyRepository;
    private CurrencyFeignClient currencyFeignClient;
    private static final Logger logger= LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyFeignClient currencyFeignClient){
        this.currencyRepository = currencyRepository;
        this.currencyFeignClient=currencyFeignClient;
    }

    @Override
    public String saveByResTemplate() {
        logger.info("Entering save() method");

        String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        CurrencyCreateDto[] currencyCreateDtos = restTemplate.getForObject(url, CurrencyCreateDto[].class);

        if (!currencyRepository.findAll().isEmpty()){
            logger.debug("Found existing currencies, will delete them");
            currencyRepository.deleteAll();
        }
        if (currencyCreateDtos != null) {
            for (CurrencyCreateDto currencyCreateDto : currencyCreateDtos) {
                logger.debug("Saving new currency: {}", currencyCreateDto);
                Currency currency = MyMapper.INSTANCE.currencyCreateDtoToCurrency(currencyCreateDto);
                Currency save = currencyRepository.save(currency);
            }
        }else {
            String errorMessage = "No data found with the provided url: " + url;
            logger.error(errorMessage);
            throw new CurrencyNotSaveException(errorMessage);
        }
        logger.info("Exiting save() method");
        return "Success";
    }

    @Override
    public String saveByOpenFeign() {
        logger.info("Entering save() method");

        CurrencyCreateDto[] currencyCreateDtos = currencyFeignClient.getCurrencies();

        if (!currencyRepository.findAll().isEmpty()){
            logger.debug("Found existing currencies, will delete them");
            currencyRepository.deleteAll();
        }
        if (currencyCreateDtos != null) {
            for (CurrencyCreateDto currencyCreateDto : currencyCreateDtos) {
                logger.debug("Saving new currency: {}", currencyCreateDto);
                Currency currency = MyMapper.INSTANCE.currencyCreateDtoToCurrency(currencyCreateDto);
                Currency save = currencyRepository.save(currency);
            }
        }else {
            String errorMessage = "No data found with the provided url: " + "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";
            logger.error(errorMessage);
            throw new CurrencyNotSaveException(errorMessage);
        }
        logger.info("Exiting save() method");
        return "Success";
    }

    @Override
    public CurrencyReadDto getByCode(String code) {
        logger.info("Entering getByCode() method with code: {}", code);
        Optional<Currency> optional = currencyRepository.findByCode(code);
        if(optional.isPresent()){
            logger.info("Exiting getByCode() method");
            return MyMapper.INSTANCE.currencyToCurrencyReadDto(optional.get());
        }else {
            String errorMessage = "No Currency found with the provided code: " + code;
            logger.error(errorMessage);
            throw new DataNotFoundException(errorMessage);
        }
    }

    @Override
    public CurrencyReadDto getByCcy(String ccy) {
        logger.info("Entering getByCcy() method with ccy: {}", ccy);
        Optional<Currency> optional = currencyRepository.findByCcy(ccy);
        if(optional.isPresent()){
            logger.info("Exiting getByCcy() method");
            return MyMapper.INSTANCE.currencyToCurrencyReadDto(optional.get());
        }else {
            String errorMessage = "No Currency found with the provided ccy: " + ccy;
            logger.error(errorMessage);
            throw new DataNotFoundException(errorMessage);
        }
    }

    @Override
    public Page<CurrencyReadDto> getByPage(Pageable pageable) {
        Page<Currency> currencies = currencyRepository.findAll(pageable);
        if(currencies.isEmpty()){
            throw new DataNotFoundException("Not found Currencies in database!");
        }
        return currencies.map(MyMapper::toCurrencyReadDto);
    }

    @Override
    public List<CurrencyReadDto> getAll() {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.isEmpty()){
            throw new DataNotFoundException("Not found Currencies in database!");
        }
        List<CurrencyReadDto> currencyReadDtos=new ArrayList<>();
        for (Currency currency : currencies) {
            CurrencyReadDto currencyReadDto = MyMapper.INSTANCE.currencyToCurrencyReadDto(currency);
            currencyReadDtos.add(currencyReadDto);
        }
        return currencyReadDtos;
    }

}

