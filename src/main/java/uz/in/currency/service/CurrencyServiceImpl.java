package uz.in.currency.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.in.currency.config.ApplicationProperties;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.entity.Currency;
import uz.in.currency.entity.User;
import uz.in.currency.exception.CommonException;
import uz.in.currency.mapper.CurrencyMapper;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.service.strategy.CurrencyStrategy;
import uz.in.currency.service.strategy.StrategyContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private final CurrencyRepository currencyRepository;
    private final StrategyContainer strategyContainer;
    private final CurrencyMapper currencyMapper;

    @Override
    public List<StandardCurrencyDTO> saveByResTemplate(String bankName) {
        logger.info("Save currency by Rest Template with param: {}", bankName);

        CurrencyStrategy strategy = strategyContainer.getStrategy(bankName);
        if (strategy == null) {
            logger.error("No bank found with provided name: {}", bankName);
            throw new CommonException("No bank found with provided name: " + bankName);
        }

        List<StandardCurrencyDTO> dtoList = strategy.getCurrenciesUsingResTemplate();
        List<StandardCurrencyDTO> standardCurrencyDTOList = saveCurrencies(bankName, dtoList);

        logger.info("Response to save by Rest Template with list of objects: {}", standardCurrencyDTOList);
        return standardCurrencyDTOList;

    }

    @Override
    public List<StandardCurrencyDTO> saveByOpenFeign(String bankName) {
        logger.info("Save Currency by Open Feign with param: {}", bankName);

        CurrencyStrategy strategy = strategyContainer.getStrategy(bankName);
        if (strategy == null) {
            logger.error("No bank found with provided name: {}", bankName);
            throw new CommonException("No bank found with provided name: " + bankName);
        }

        List<StandardCurrencyDTO> dtoList = strategy.getCurrenciesUsingOpenFeign();
        List<StandardCurrencyDTO> standardCurrencyDTOList = saveCurrencies(bankName, dtoList);

        logger.info("Response to save currency by Open Feign with list of objects: {}", standardCurrencyDTOList);
        return standardCurrencyDTOList;
    }

    public List<StandardCurrencyDTO> saveCurrencies(String bankName, List<StandardCurrencyDTO> dtoList) {
        logger.info("Save currencies with param: {}", bankName);

        if (dtoList.isEmpty()) {
            logger.error("No currency found with provided bank: {}", bankName);
            throw new CommonException("No currency found with provided bank: " + bankName);
        }

        List<Currency> saveAll = currencyRepository.saveAll(currencyMapper.toEntity(dtoList));
        List<StandardCurrencyDTO> standardCurrencyDTOList = currencyMapper.toDTO(saveAll);

        logger.info("Response to save currencies with list of objects: {}", standardCurrencyDTOList);
        return standardCurrencyDTOList;
    }

    @Override
    public StandardCurrencyDTO getByCode(Long code) {
        logger.info("Request to getByCode() method with code: {}", code);
        Optional<Currency> currencyOptional = currencyRepository.findByCode(code);

        if (currencyOptional.isEmpty()) {
            logger.error("No Currency found with the provided code: {}", code);
            throw new CommonException("No Currency found with the provided code: " + code);
        }

        StandardCurrencyDTO dto = currencyMapper.toDTO(currencyOptional.get());
        logger.info("Response to getByCode() method with data: {}", dto);
        return dto;
    }


    @Override
    public StandardCurrencyDTO getByCcy(String ccy) {
        logger.info("Request to getByCcy() method with ccy: {}", ccy);
        Optional<Currency> currencyOptional = currencyRepository.findByCcy(ccy);

        if (currencyOptional.isEmpty()) {
            logger.error("No Currency found with the provided ccy: {}", ccy);
            throw new CommonException("No Currency found with the provided ccy: " + ccy);
        }
        StandardCurrencyDTO dto = currencyMapper.toDTO(currencyOptional.get());
        logger.info("Response to getByCcy() method with data: {}", dto);
        return dto;
    }

    @Override
    public Page<StandardCurrencyDTO> getByPage(Pageable pageable) {
        logger.info("Entering getByPage() method");
        Page<Currency> currencies = currencyRepository.findAll(pageable);

        if (currencies.isEmpty()) {
            logger.error("No Currency found with the provided page: {}", pageable);
            throw new CommonException("No Currency found with the provided page");
        }
        Page<StandardCurrencyDTO> dtoPage = currencies.map(currencyMapper::toDTO);
        logger.info("Response to getByCode() method with page: {}", dtoPage);
        return dtoPage;

    }

    @Override
    public List<StandardCurrencyDTO> getAll() {
        logger.info("Entering getAll() method");
        List<Currency> currencies = currencyRepository.findAll();

        if (currencies.isEmpty()) {
            String errorMessage = "No Currency found in database";
            logger.error(errorMessage);
            throw new CommonException(errorMessage);
        }

        List<StandardCurrencyDTO> dtoList = currencyMapper.toDTO(currencies);
        logger.info("Response to getByCode() method with list: {}", dtoList);
        return dtoList;
    }
}

