package uz.in.currency.service.strategy;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.config.ApplicationProperties;
import uz.in.currency.dto.CurrencyDTOFromNBU;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.exception.CommonException;
import uz.in.currency.service.feign.CurrencyFromNBUFeignClient;
import uz.in.currency.util.ObjectMapperUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uz.in.currency.enumeration.StrategyName.NBU;

@Service
@RequiredArgsConstructor
public class NBUStrategyService implements CurrencyStrategy {
    private static final Logger logger = LoggerFactory.getLogger(NBUStrategyService.class);
    private final ApplicationProperties applicationProperties;
    private final CurrencyFromNBUFeignClient feignClient;
    private final String URL = "https://nbu.uz/uz/exchange-rates/json/";

    @Override
    public List<StandardCurrencyDTO> getCurrenciesUsingResTemplate() {
        try {
            logger.info("Request to get currency by Rest Template with url: {}", URL);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            ResponseEntity<List<CurrencyDTOFromNBU>> response = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            String exception = "";
            if (response.getBody() == null || response.getBody().isEmpty()) {
                exception = "Response is null";
                logger.warn(exception);
                throw new CommonException(exception);
            }

            List<StandardCurrencyDTO> standardDTOList = new ArrayList<>(response.getBody());
            setCodeAndDate(standardDTOList, applicationProperties.getCcyCodes());

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
            logger.info("Request to get currency from NBU by Open Feign with url: {}", URL);
            List<CurrencyDTOFromNBU> nbuList = feignClient.getCurrencies();

            String exception = "";
            if (nbuList == null || nbuList.isEmpty()) {
                exception = "Response is null";
                logger.warn(exception);
                throw new CommonException(exception);
            }

            setCodeAndDate(new ArrayList<>(nbuList), ObjectMapperUtil.getCcyCodesFromJson());

            logger.info("Response to get currency by Open Feign with list: {}", nbuList);
            return new ArrayList<>(nbuList);
        } catch (CommonException e) {
            logger.error("An error occurred during get currency by Open Feign", e);
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public String strategyName() {
        return NBU.getName();
    }

    private void setCodeAndDate(List<StandardCurrencyDTO> currencyDTOList, Map<String, String> ccyCodes) {

        String exception = "";
        if (ccyCodes == null) {
            exception = "ccyCodes is null";
            logger.warn(exception);
            throw new CommonException(exception);
        }

        currencyDTOList.forEach(currency -> {
            currency.setCode(ccyCodes.get(currency.getCcy()));
            currency.setDate(currency.getDate().substring(0, 10));
        });
    }
}
