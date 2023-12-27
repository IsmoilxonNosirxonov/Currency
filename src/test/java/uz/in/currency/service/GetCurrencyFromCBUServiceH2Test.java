package uz.in.currency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.dto.CurrencyDTOFromCBU;
import uz.in.currency.dto.StandartCurrencyDTO;
import uz.in.currency.exception.GetCurrencyException;
import uz.in.currency.feign.CurrencyFromCBUFeignClient;
import uz.in.currency.service.strategy.GetCurrencyFromCBUService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
public class GetCurrencyFromCBUServiceH2Test {

    @Mock
    private CurrencyFromCBUFeignClient feignClient;
    @Mock
    private RestTemplate restTemplate;
    private GetCurrencyFromCBUService getCurrencyFromCBUService;
    private static final String CBU_URL = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";

    @Before
    public void setUp(){
        getCurrencyFromCBUService=new GetCurrencyFromCBUService(feignClient);
    }

    @Test
    public void getCurrenciesUsingResTemplateSuccessfully(){

        CurrencyDTOFromCBU[] dtoFromCBUS=new CurrencyDTOFromCBU[1];
        dtoFromCBUS[0]= CurrencyDTOFromCBU.builder()
                .id(21L)
                .code("978")
                .ccy("EUR")
                .ccyNm_RU("ЕBPO")
                .ccyNm_UZ("EVRO")
                .ccyNm_UZC("EBPO")
                .ccyNm_EN("EURO")
                .nominal("1")
                .rate("13566.97")
                .diff("91.77")
                .date("18.12.2023")
                .build();

        when(restTemplate.getForObject(CBU_URL,CurrencyDTOFromCBU[].class)).thenReturn(dtoFromCBUS);

        List<StandartCurrencyDTO> response = getCurrencyFromCBUService.getCurrenciesUsingResTemplate();
        assertFalse(response.isEmpty());
        assertEquals(75,response.size());
    }

    @Test
    public void getCurrenciesUsingOpenFeignSuccessfully(){

        CurrencyDTOFromCBU[] dtoFromCBUS=new CurrencyDTOFromCBU[1];
        dtoFromCBUS[0]= CurrencyDTOFromCBU.builder()
                .id(21L)
                .code("978")
                .ccy("EUR")
                .ccyNm_RU("ЕBPO")
                .ccyNm_UZ("EVRO")
                .ccyNm_UZC("EBPO")
                .ccyNm_EN("EURO")
                .nominal("1")
                .rate("13566.97")
                .diff("91.77")
                .date("18.12.2023")
                .build();

        when(feignClient.getCurrencies()).thenReturn(dtoFromCBUS);

        List<StandartCurrencyDTO> response = getCurrencyFromCBUService.getCurrenciesUsingOpenFeign();
        assertFalse(response.isEmpty());
        assertEquals(1,response.size());
    }

    @Test
    public void getCurrenciesUsingOpenFeignGetCurrencyException(){

        when(feignClient.getCurrencies()).thenReturn(null);

        assertThrows(GetCurrencyException.class, ()->getCurrencyFromCBUService.getCurrenciesUsingOpenFeign());
    }
}
