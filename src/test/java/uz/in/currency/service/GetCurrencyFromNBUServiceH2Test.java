package uz.in.currency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.dto.CurrencyDTOFromNBU;
import uz.in.currency.dto.StandartCurrencyDTO;
import uz.in.currency.exception.GetCurrencyException;
import uz.in.currency.feign.CurrencyFromNBUFeignClient;
import uz.in.currency.service.strategy.GetCurrencyFromNBUService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
public class GetCurrencyFromNBUServiceH2Test {
    @Mock
    private CurrencyFromNBUFeignClient feignClient;
    @Mock
    private RestTemplate restTemplate;
    private GetCurrencyFromNBUService getCurrencyFromNBUService;
    private static final String NBU_URL = "https://nbu.uz/uz/exchange-rates/json/";

    @Before
    public void setUp(){
        getCurrencyFromNBUService=new GetCurrencyFromNBUService(feignClient);
    }

    @Test
    public void getCurrenciesUsingResTemplateSuccessfully(){

        CurrencyDTOFromNBU[] dtoFromNBUS=new CurrencyDTOFromNBU[1];
        dtoFromNBUS[0]= CurrencyDTOFromNBU.builder()
                .code("978")
                .ccy("EUR")
                .ccyNm_UZ("EVRO")
                .rate("13566.97")
                .date("18.12.2023")
                .build();

        when(restTemplate.getForObject(NBU_URL, CurrencyDTOFromNBU[].class)).thenReturn(dtoFromNBUS);

        List<StandartCurrencyDTO> response = getCurrencyFromNBUService.getCurrenciesUsingResTemplate();
        assertFalse(response.isEmpty());
        assertEquals(24,response.size());
    }

    @Test
    public void getCurrenciesUsingOpenFeignSuccessfully(){

        CurrencyDTOFromNBU[] dtoFromNBUS=new CurrencyDTOFromNBU[1];
        dtoFromNBUS[0]= CurrencyDTOFromNBU.builder()
                .code("978")
                .ccy("EUR")
                .ccyNm_UZ("EVRO")
                .rate("13566.97")
                .date("18.12.2023")
                .build();

        when(feignClient.getCurrencies()).thenReturn(dtoFromNBUS);

        List<StandartCurrencyDTO> response = getCurrencyFromNBUService.getCurrenciesUsingOpenFeign();
        assertFalse(response.isEmpty());
        assertEquals(1,response.size());
    }

    @Test
    public void getCurrenciesUsingOpenFeignGetCurrencyException(){

        when(feignClient.getCurrencies()).thenReturn(null);

        assertThrows(GetCurrencyException.class, ()->getCurrencyFromNBUService.getCurrenciesUsingOpenFeign());
    }
}
