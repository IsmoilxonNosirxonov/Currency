package uz.in.currency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import uz.in.currency.dto.CurrencyDTOFromCBU;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.exception.CommonException;
import uz.in.currency.service.feign.CurrencyFromCBUFeignClient;
import uz.in.currency.service.strategy.CBUStrategyService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CBUStrategyServiceJunitTest {

    @Mock
    private CurrencyFromCBUFeignClient feignClient;
    private CBUStrategyService cbuStrategyService;

    @Value("${exchange.cbu.url}")
    private String URL;

    @Before
    public void setUp() {
        cbuStrategyService = new CBUStrategyService(feignClient, URL);
    }

    @Test
    public void successfullyTestGetCurrenciesUsingRestTemplate() {

        List<StandardCurrencyDTO> cbuList = cbuStrategyService.getCurrenciesUsingResTemplate();

        assertNotNull(cbuList);
        assertFalse(cbuList.isEmpty());
        assertEquals(75, cbuList.size());
        assertEquals("840", cbuList.get(0).getCode());
        assertEquals("USD", cbuList.get(0).getCcy());
    }

//    @Test
//    public void failedTestGetCurrenciesUsingRestTemplate() {
//
//        assertThrows(CommonException.class, () -> cbuStrategyService.getCurrenciesUsingResTemplate());
//    }

    @Test
    public void successfullyTestGetCurrenciesUsingOpenFeign() {
        CurrencyDTOFromCBU currencyDTOFromCBU = new CurrencyDTOFromCBU(1L, "test", "test", "test", "1", "1", "test", "test", "test", "1", "1");
        when(feignClient.getCurrencies()).thenReturn(List.of(currencyDTOFromCBU));

        List<StandardCurrencyDTO> cbuList = cbuStrategyService.getCurrenciesUsingOpenFeign();

        assertNotNull(cbuList);
        assertEquals(1, cbuList.size());
        assertEquals("test", cbuList.get(0).getCode());
        assertEquals("1", cbuList.get(0).getRate());
    }

    @Test
    public void failedTestGetCurrenciesUsingOpenFeign() {

        when(feignClient.getCurrencies()).thenReturn(new ArrayList<>());

        assertThrows(CommonException.class, () -> cbuStrategyService.getCurrenciesUsingOpenFeign());
    }
}
