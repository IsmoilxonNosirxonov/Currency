package uz.in.currency.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.domain.dto.CurrencyCreateDto;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.feign.CurrencyFeignClient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CurrencyControllerIntegrationTest2 {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private CurrencyFeignClient currencyFeignClient;

    @Test
    public void testSaveByRestTemplate() throws Exception {
        CurrencyCreateDto[] currencyCreateDtos=new CurrencyCreateDto[1];
        currencyCreateDtos[0]= CurrencyCreateDto.builder()
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

        when(restTemplate.getForObject("https://cbu.uz/uz/arkhiv-kursov-valyut/json/",CurrencyCreateDto[].class)).thenReturn(currencyCreateDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/api/v1/save-all-currencies-by-rest-template"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success"))
                .andDo(print());
    }
}
