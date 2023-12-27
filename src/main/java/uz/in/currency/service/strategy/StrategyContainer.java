package uz.in.currency.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StrategyContainer {

    private final CurrencyStrategy unknownStrategy;
    public final Map<String, CurrencyStrategy> currencyStrategyMap = new HashMap<>();


    public StrategyContainer(List<CurrencyStrategy> currencyStrategies) {
        this.unknownStrategy = new UnknownStrategyService();
        currencyStrategies.forEach((currencyStrategy -> currencyStrategyMap.put(currencyStrategy.strategyName(), currencyStrategy)));
    }

    public CurrencyStrategy getStrategy(String strategyName) {
        return currencyStrategyMap.getOrDefault(strategyName, unknownStrategy);
    }
}
