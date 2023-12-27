package uz.in.currency.service.strategy;

import org.springframework.stereotype.Service;
import uz.in.currency.dto.StandardCurrencyDTO;

import java.util.List;

@Service
public interface CurrencyStrategy {
    List<StandardCurrencyDTO> getCurrenciesUsingResTemplate();

    List<StandardCurrencyDTO> getCurrenciesUsingOpenFeign();

    String strategyName();
}
