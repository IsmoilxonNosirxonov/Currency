package uz.in.currency.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uz.in.currency.config.JwtService;
import uz.in.currency.dto.StandartCurrencyDTO;
import uz.in.currency.entity.User;
import uz.in.currency.exception.DataNotFoundException;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.role.UserRole;
import uz.in.currency.service.CurrencyService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private static String jwtToken;

    @BeforeEach
    public void setUp(){
        User testUser = new User(UUID.randomUUID(), "Test", "test@gmail.com", "test", UserRole.USER, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(testUser);

        jwtToken=jwtService.generateToken(testUser);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void successfullyTestSaveByRestTemplate() throws Exception {

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/save-all-currencies-by-rest-template/{bankName}","cbu")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var currencyDTOList= objectMapper.readValue(contentAsString, new TypeReference<List<StandartCurrencyDTO>>() {});
        assertFalse(currencyDTOList.isEmpty());
        assertEquals(75,currencyDTOList.size());

        System.out.println("RESULT "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyDTOList));
    }

    @Test
    public void failedTestSaveByRestTemplate() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/save-all-currencies-by-rest-template/{bankName}","cbu")
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void successfullyTestSaveByOpenFeign() throws Exception {

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/save-all-currencies-by-open-feign/{bankName}","cbu")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var currencyDTOList= objectMapper.readValue(contentAsString, new TypeReference<List<StandartCurrencyDTO>>() {});
        assertFalse(currencyDTOList.isEmpty());
        assertEquals(75,currencyDTOList.size());

        System.out.println("RESULT "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyDTOList));
    }

    @Test
    public void failedTestSaveByOpenFeign() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/save-all-currencies-by-open-feign/{banName}", "nbu")
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    void successfullyTestGetByCode() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-currency-by-code/{code}", "840")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var standartCurrencyDTO = objectMapper.readValue(contentAsString, new TypeReference<StandartCurrencyDTO>() {
        });

        assertNotNull(standartCurrencyDTO);
        assertEquals("840",standartCurrencyDTO.getCode());
        assertEquals("USD",standartCurrencyDTO.getCcy());

        System.out.println("RESULT "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(standartCurrencyDTO));
    }

    @Test
    void failedTestGetByCode() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-currency-by-code/{code}", "840")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("No Currency found with the provided code: 840" ,resolvedException.getMessage());
                })
                .andDo(print());

    }

    @Test
    void successfullyTestGetByCcy() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-currency-by-ccy/{ccy}", "EUR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var standartCurrencyDTO = objectMapper.readValue(contentAsString, new TypeReference<StandartCurrencyDTO>() {});

        assertNotNull(standartCurrencyDTO);
        assertEquals("EUR",standartCurrencyDTO.getCcy());
        assertEquals("978",standartCurrencyDTO.getCode());

        System.out.println("RESULT "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(standartCurrencyDTO));
    }

    @Test
    void failedTestGetByCcy() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-currency-by-ccy/{ccy}", "EUR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("No Currency found with the provided ccy: EUR" ,resolvedException.getMessage());
                })
                .andDo(print());

    }

    @Test
    void successfullyTestGetByPage() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-currencies-by-page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var dtoPage = objectMapper.readValue(contentAsString, new TypeReference<Page<StandartCurrencyDTO>>() {});

        assertNotNull(dtoPage);
        assertEquals(75,dtoPage.getTotalElements());
        assertEquals(8,dtoPage.getTotalPages());

        System.out.println("RESULT "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoPage));
    }

    @Test
    void failedTestGetByPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-currencies-by-page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("No Currency found in database" ,resolvedException.getMessage());
                })
                .andDo(print());
    }

    @Test
    void successfullyTestGetAll() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-all-currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);

        var dtoList = objectMapper.readValue(contentAsString, new TypeReference<List<StandartCurrencyDTO>>() {});

        assertNotNull(dtoList);
        assertEquals(75,dtoList.size());

        System.out.println("RESULT "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoList));
    }

    @Test
    void failedTestGetAll() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-all-currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("No Currency found in database" ,resolvedException.getMessage());
                })
                .andDo(print());

    }
}
