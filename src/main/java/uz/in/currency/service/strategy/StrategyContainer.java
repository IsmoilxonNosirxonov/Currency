package uz.in.currency.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StrategyContainer {

    private final Map<String, CurrencyStrategy> currencyStrategyMap=new HashMap<>();


    public StrategyContainer(List<CurrencyStrategy> currencyStrategies){
        currencyStrategies.forEach((currencyStrategy -> currencyStrategyMap.put(currencyStrategy.getClass().getName(),currencyStrategy)));
    }

    public CurrencyStrategy getStrategy(String strategyName){
        return currencyStrategyMap.getOrDefault(strategyName,null);
    }
}
