package uz.in.currency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uz.in.currency.config.JwtService;
import uz.in.currency.domain.dto.CurrencyCreateDto;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.entity.User;
import uz.in.currency.domain.role.UserRole;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.service.currency.CurrencyService;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CurrencyControllerIntegrationTest2 {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CurrencyService currencyService;

    private static final String API = "http://localhost:8083/api/v1";
    private static String jwtToken;

    @BeforeEach
    public void getToken(){
        jwtToken=jwtService.generateToken(new User(UUID.randomUUID(),"Test","test@example.com", "test", UserRole.ADMIN, LocalDateTime.now(),LocalDateTime.now()));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
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


        mockMvc.perform(MockMvcRequestBuilders.get(API + "/save-all-currencies-by-rest-template")
                        .header("Bearer ", jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success"))
                .andDo(print());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSaveByOpenFeign() throws Exception {

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

        mockMvc.perform(MockMvcRequestBuilders.get(API+"/save-all-currencies-by-open-feign")
                        .header("Bearer ", jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void testGetByCode() throws Exception {

        currencyService.saveByOpenFeign();

        mockMvc.perform(MockMvcRequestBuilders.get(API+"/get-currency-by-code/{code}", "840")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Bearer ", jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }

    @Test
    @WithMockUser
    void testGetByCcy() throws Exception {

        currencyService.saveByOpenFeign();

        mockMvc.perform(MockMvcRequestBuilders.get(API+"/get-currency-by-ccy/{ccy}", "EUR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Bearer ", jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }

    @Test
    @WithMockUser
    void testGetByPage() throws Exception {

        currencyService.saveByOpenFeign();

        mockMvc.perform(MockMvcRequestBuilders.get(API + "/get-currencies-by-page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Bearer ", jwtToken)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }

    @Test
    @WithMockUser
    void testGetAll() throws Exception {

        currencyService.saveByOpenFeign();

        mockMvc.perform(MockMvcRequestBuilders.get(API + "/get-all-currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Bearer ", jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }
}
