package uz.in.currency.service.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.dto.CurrencyDTOFromNBU;
import uz.in.currency.dto.StandartCurrencyDTO;
import uz.in.currency.exception.GetCurrencyException;
import uz.in.currency.feign.CurrencyFromNBUFeignClient;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetCurrencyFromNBUService implements CurrencyStrategy {

    private final
    CurrencyFromNBUFeignClient feignClient;
    private static final Logger logger= LoggerFactory.getLogger(GetCurrencyFromNBUService.class);
    private String url = "https://nbu.uz/uz/exchange-rates/json/";
    @Override
    public List<StandartCurrencyDTO> getCurrenciesUsingResTemplate() {
        logger.info("Entering getCurrenciesUsingResTemplate() method");

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        CurrencyDTOFromNBU[] currencies = restTemplate.getForObject(url, CurrencyDTOFromNBU[].class);

        if (currencies==null){
            String errorMessage = "No data found with the provided url: " + url;
            logger.error(errorMessage);
            throw new GetCurrencyException(errorMessage);
        }

        logger.info("Exiting getCurrenciesUsingResTemplate() method");
        return List.of(Objects.requireNonNull(currencies));
    }

    @Override
    public List<StandartCurrencyDTO> getCurrenciesUsingOpenFeign() {
        logger.info("Entering getCurrenciesUsingOpenFeign() method");

        CurrencyDTOFromNBU[] currencies = feignClient.getCurrencies();

        if (currencies==null){
            String errorMessage = "No data found with the provided url: " + url;
            logger.error(errorMessage);
            throw new GetCurrencyException(errorMessage);
        }

        logger.info("Exiting getCurrenciesUsingOpenFeign() method");
        return List.of(Objects.requireNonNull(currencies));
    }
}
