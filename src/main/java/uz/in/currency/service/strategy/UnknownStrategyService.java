package uz.in.currency.service.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.exception.CommonException;

import java.util.List;

import static uz.in.currency.enumeration.StrategyName.UNKNOWN;

@Service
public class UnknownStrategyService implements CurrencyStrategy {
    private static final Logger logger = LoggerFactory.getLogger(CBUStrategyService.class);

    @Override
    public List<StandardCurrencyDTO> getCurrenciesUsingResTemplate() {
        String errorMessage = "This bank was not found in the database!";
        logger.error(errorMessage);
        throw new CommonException(errorMessage);
    }

    @Override
    public List<StandardCurrencyDTO> getCurrenciesUsingOpenFeign() {
        String errorMessage = "This bank was not found in the database!";
        logger.error(errorMessage);
        throw new CommonException(errorMessage);
    }

    @Override
    public String strategyName() {
        return UNKNOWN.getName();
    }
}
