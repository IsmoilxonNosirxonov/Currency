package uz.in.currency.service.strategy;

import org.springframework.stereotype.Service;
import uz.in.currency.dto.StandartCurrencyDTO;

import java.util.List;

@Service
public interface CurrencyStrategy {
    List<StandartCurrencyDTO> getCurrenciesUsingResTemplate();
    List<StandartCurrencyDTO> getCurrenciesUsingOpenFeign();
}
