package uz.in.currency.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.in.currency.config.ApplicationProperties;
import uz.in.currency.dto.StandartCurrencyDTO;
import uz.in.currency.entity.Currency;
import uz.in.currency.exception.DataNotFoundException;
import uz.in.currency.mapper.CurrencyMapper;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.service.strategy.CurrencyStrategy;
import uz.in.currency.service.strategy.GetCurrencyFromCBUService;
import uz.in.currency.service.strategy.GetCurrencyFromNBUService;
import uz.in.currency.service.strategy.StrategyContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{

    private final ApplicationProperties applicationProperties;
    private final CurrencyRepository currencyRepository;
    private final StrategyContainer strategyContainer;
    private final CurrencyMapper currencyMapper;
    private static final Logger logger= LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Override
    public List<StandartCurrencyDTO> saveByResTemplate(String bankName) {
        logger.info("Entering saveByResTemplate() method");

        List<StandartCurrencyDTO> currencyDTOS=new ArrayList<>();

        String name="";
        if(bankName.equalsIgnoreCase("CBU")){
            name= GetCurrencyFromCBUService.class.getName();
            currencyDTOS=strategyContainer.getStrategy(name).getCurrenciesUsingResTemplate();
        }else if (bankName.equalsIgnoreCase("NBU")){
            name= GetCurrencyFromNBUService.class.getName();
            currencyDTOS=strategyContainer.getStrategy(name).getCurrenciesUsingResTemplate();
            applicationProperties.test(currencyDTOS);

        }

        for (StandartCurrencyDTO currencyDTO : currencyDTOS) {
            logger.debug("Saving new currency: {}", currencyDTO);
            currencyDTO.setDate(currencyDTO.getDate().substring(0,10));
            Currency currency = currencyMapper.toEntity(currencyDTO);
            Currency save = currencyRepository.save(currency);
            System.out.println();
        }

        logger.info("Exiting saveByResTemplate() method");
        return currencyDTOS;
    }

    @Override
    public List<StandartCurrencyDTO> saveByOpenFeign(String bankName) {
        logger.info("Entering saveByByOpenFeign() method");

        List<StandartCurrencyDTO> currencyDTOS=new ArrayList<>();

        String name="";
        if(bankName.equalsIgnoreCase("CBU")){
            name= GetCurrencyFromCBUService.class.getName();
            currencyDTOS = strategyContainer.getStrategy(name).getCurrenciesUsingOpenFeign();
        }else if (bankName.equalsIgnoreCase("NBU")){
            name= GetCurrencyFromNBUService.class.getName();
            currencyDTOS = strategyContainer.getStrategy(name).getCurrenciesUsingOpenFeign();
            applicationProperties.test(currencyDTOS);
        }else {
            throw new IllegalArgumentException("Bank name not entry in data base");
        }

        for (StandartCurrencyDTO currencyDTO : currencyDTOS) {
            logger.debug("Saving new currency: {}", currencyDTO);
            currencyDTO.setDate(currencyDTO.getDate().substring(0,10));
            Currency save = currencyRepository.save(currencyMapper.toEntity(currencyDTO));
        }

        logger.info("Exiting saveByByOpenFeign() method");
        return currencyDTOS;
    }

    @Override
    public StandartCurrencyDTO getByCode(Integer code) {
        logger.info("Entering getByCode() method with code: {}", code);
        Optional<Currency> currencyOptional = currencyRepository.findByCode(code);

        if(currencyOptional.isEmpty()){
            String errorMessage = "No Currency found with the provided code: " + code;
            logger.error(errorMessage);
            throw new DataNotFoundException(errorMessage);
        }

        logger.info("Exiting getByCode() method");
        return currencyMapper.toDTO(currencyOptional.get());
    }

    @Override
    public StandartCurrencyDTO getByCcy(String ccy) {
        logger.info("Entering getByCcy() method with ccy: {}", ccy);
        Optional<Currency> currencyOptional = currencyRepository.findByCcy(ccy);

        if(currencyOptional.isEmpty()) {
            String errorMessage = "No Currency found with the provided ccy: " + ccy;
            logger.error(errorMessage);
            throw new DataNotFoundException(errorMessage);
        }

        logger.info("Exiting getByCcy() method");
        return currencyMapper.toDTO(currencyOptional.get());
    }

    @Override
    public Page<StandartCurrencyDTO> getByPage(Pageable pageable) {
        logger.info("Entering getByPage() method");
        Page<Currency> currencies = currencyRepository.findAll(pageable);

        if(currencies.isEmpty()) {
            String errorMessage = "No Currency found in database";
            logger.error(errorMessage);
            throw new DataNotFoundException(errorMessage);
        }

        logger.info("Exiting getByPage() method");
        return currencies.map(currencyMapper::toDTO);
    }

    @Override
    public List<StandartCurrencyDTO> getAll() {
        logger.info("Entering getAll() method");
        List<Currency> currencies = currencyRepository.findAll();

        if (currencies.isEmpty()) {
            String errorMessage = "No Currency found in database";
            logger.error(errorMessage);
            throw new DataNotFoundException(errorMessage);
        }
        logger.info("Exiting getAll() method");
        return currencyMapper.toDTO(currencies);
    }
}

