package uz.in.currency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.config.ApplicationProperties;
import uz.in.currency.dto.CurrencyDTOFromNBU;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.exception.CommonException;
import uz.in.currency.service.feign.CurrencyFromNBUFeignClient;
import uz.in.currency.service.strategy.NBUStrategyService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
public class NBUStrategyServiceJunitTest {

    private static final String NBU_URL = "https://nbu.uz/uz/exchange-rates/json/";
    @Mock
    private CurrencyFromNBUFeignClient feignClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApplicationProperties applicationProperties;
    private NBUStrategyService nbuStrategyService;

    @Before
    public void setUp() {
        nbuStrategyService = new NBUStrategyService(applicationProperties,feignClient);
    }

    @Test
    public void getCurrenciesUsingRestTemplateSuccessfully() {

        List<StandardCurrencyDTO> nbuList = nbuStrategyService.getCurrenciesUsingResTemplate();

        assertNotNull(nbuList);
        assertEquals(24, nbuList.size());
        assertEquals("784", nbuList.get(0).getCode());
        assertEquals("AED", nbuList.get(0).getCcy());
    }

//    @Test
//    public void getCurrenciesUsingRestTemplateFailed() {
//
//        CommonException commonException = assertThrows(CommonException.class, () -> nbuStrategyService.getCurrenciesUsingResTemplate());
//    }

    @Test
    public void getCurrenciesUsingOpenFeignSuccessfully() {
        CurrencyDTOFromNBU currencyDTOFromNBU = new CurrencyDTOFromNBU("1", "1", "test","test", "test", "1", "2022-01-01T12:34:56");

        List<CurrencyDTOFromNBU> list = List.of(currencyDTOFromNBU);
        list.get(0).setDate(list.get(0).getDate().substring(0,10));
        when(feignClient.getCurrencies()).thenReturn(list);

        List<StandardCurrencyDTO> nbuList = nbuStrategyService.getCurrenciesUsingOpenFeign();

        assertNotNull(nbuList);
        assertEquals(1, nbuList.size());
        assertEquals("test",nbuList.get(0).getCcy());
        assertEquals("1", nbuList.get(0).getRate());
    }

    @Test
    public void getCurrenciesUsingOpenFeignFailed() {
        CommonException commonException = assertThrows(CommonException.class, () -> nbuStrategyService.getCurrenciesUsingOpenFeign());
        assertEquals("Response is null",commonException.getMessage());
    }
}
