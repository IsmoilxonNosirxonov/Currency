package uz.in.currency.config;

import feign.Feign;
import org.mapstruct.factory.Mappers;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.dto.CurrencyDTOFromCBU;
import uz.in.currency.dto.CurrencyDTOFromNBU;
import uz.in.currency.mapper.CurrencyMapper;
import uz.in.currency.mapper.UserMapper;
import uz.in.currency.service.feign.CurrencyFromCBUFeignClient;
import uz.in.currency.service.feign.CurrencyFromNBUFeignClient;
import uz.in.currency.service.strategy.CBUStrategyService;
import uz.in.currency.service.strategy.CurrencyStrategy;
import uz.in.currency.service.strategy.StrategyContainer;

import java.util.List;

@Configuration
public class TestConfig {

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public CurrencyMapper currencyMapper() {
        return Mappers.getMapper(CurrencyMapper.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StrategyContainer strategyContainer(List<CurrencyStrategy> currencyStrategies) {
        return new StrategyContainer(currencyStrategies);
    }
}
