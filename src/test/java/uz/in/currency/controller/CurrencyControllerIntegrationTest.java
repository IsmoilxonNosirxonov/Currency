package uz.in.currency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uz.in.currency.config.JwtService;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.entity.Currency;
import uz.in.currency.service.currency.CurrencyService;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(value = CurrencyController.class,includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtService.class))
public class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyController currencyController;

    @MockBean
    private CurrencyService currencyService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    public void testSaveByRestTemplate() throws Exception {

        when(currencyService.saveByResTemplate()).thenReturn("Success");

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/api/v1/save-all-currencies-by-rest-template"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success"))
                .andDo(print());
    }

    @Test
    public void testSaveByOpenFeign() throws Exception {

        when(currencyService.saveByOpenFeign()).thenReturn("Success");

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/api/v1/save-all-currencies-by-open-feign"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success"))
                .andDo(print());
    }

    @Test
    void testGetByCode() throws Exception {

        when(currencyService.getByCode(anyString())).thenReturn(new CurrencyReadDto());

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/api/v1/get-currency-by-code/{code}", "840")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testGetByCcy() throws Exception{

        when(currencyService.getByCcy(anyString())).thenReturn(new CurrencyReadDto());

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/api/v1/get-currency-by-ccy/{ccy}","USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testGetByPage() throws Exception{

        CurrencyReadDto currencyReadDto=CurrencyReadDto.builder()
                .code("840")
                .ccy("USD")
                .ccyNm_RU("Доллар США")
                .ccyNm_UZ("AQSH dollari")
                .ccyNm_UZC("АК,Ш Доллари")
                .ccyNm_EN("US Dollar")
                .nominal("1")
                .rate("12377.49")
                .diff("19.46")
                .date("18.12.2023")
                .build();
        Page<CurrencyReadDto> page=new PageImpl<>(List.of(currencyReadDto));
        when(currencyService.getByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/api/v1/get-currencies-by-page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "code")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }

    @Test
    void testGetAll() throws Exception{

        List<CurrencyReadDto> currencyReadDtoList=new ArrayList<>();
        when(currencyService.getAll()).thenReturn(currencyReadDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/api/v1/get-all-currencies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}
