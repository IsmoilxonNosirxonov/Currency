package uz.in.currency.service.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.dto.CurrencyDTOFromCBU;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.exception.CommonException;
import uz.in.currency.service.feign.CurrencyFromCBUFeignClient;

import java.util.ArrayList;
import java.util.List;

import static uz.in.currency.enumeration.StrategyName.CBU;

@Service
@RequiredArgsConstructor
public class CBUStrategyService implements CurrencyStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CBUStrategyService.class);
    private final CurrencyFromCBUFeignClient feignClient;
    @Value("${exchange.cbu.url}")
    private final String URL;

    @Override
    public List<StandardCurrencyDTO> getCurrenciesUsingResTemplate() {
        try {
            logger.info("Request to get currency by Rest Template with url: {}", URL);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            ResponseEntity<List<CurrencyDTOFromCBU>> response = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getBody() == null) {
                String exception = "Response is null";
                logger.warn(exception);
                throw new CommonException(exception);
            }

            List<StandardCurrencyDTO> standardDTOList = new ArrayList<>(response.getBody());

            logger.debug("Response to get currency by Rest Template with list: {}", standardDTOList);
            return standardDTOList;
        } catch (CommonException e) {
            logger.error("An error occurred during get currency by Rest Template", e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public List<StandardCurrencyDTO> getCurrenciesUsingOpenFeign() {
        try {
            logger.info("Request to get currency by Open Feign with url: {}", URL);

            List<CurrencyDTOFromCBU> cbuList = feignClient.getCurrencies();

            if (cbuList == null || cbuList.isEmpty()) {
                String exception = "Response is null";
                logger.warn(exception);
                throw new CommonException(exception);
            }

            logger.info("Response to get currency by Open Feign with list: {}", cbuList);
            return new ArrayList<>(cbuList);
        } catch (CommonException e) {
            logger.error("An error occurred during get currency by Open Feign", e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public String strategyName() {
        return CBU.getName();
    }

}

