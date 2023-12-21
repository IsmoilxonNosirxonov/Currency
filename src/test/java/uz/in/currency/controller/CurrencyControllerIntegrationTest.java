package uz.in.currency.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uz.in.currency.config.JwtService;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.entity.User;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.domain.role.UserRole;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.service.currency.CurrencyService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void setUp() {
        User testUser = new User(UUID.randomUUID(), "Test", "test@gmail.com", "test", UserRole.ADMIN, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(testUser);

        jwtToken = jwtService.generateToken(testUser);
    }

    @Test
    public void successfullyTestSaveByRestTemplate() throws Exception {
        String contentAsString = mockMvc.perform(
                get("/api/v1/save-all-currencies-by-rest-template")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        assertEquals("Success", contentAsString);

        System.out.println("RESULT " + contentAsString);
    }

    @Test
    public void failedTestSaveByRestTemplate() throws Exception {
        mockMvc.perform(
                get("/api/v1/save-all-currencies-by-rest-template")
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    public void successfullyTestSaveByOpenFeign() throws Exception {
        String contentAsString = mockMvc.perform(
                get("/api/v1/save-all-currencies-by-open-feign")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        assertEquals("Success", contentAsString);

        System.out.println("RESULT " + contentAsString);
    }

    @Test
    public void failedTestSaveByOpenFeign() throws Exception {

        mockMvc.perform(
                get("/api/v1/save-all-currencies-by-open-feign")
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    void successfullyTestGetByCode() throws Exception {
        currencyService.saveByOpenFeign();

        String contentAsString = mockMvc.perform(
                get("/api/v1/get-currency-by-code/{code}", "840")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var currencyReadDto = objectMapper.readValue(contentAsString, new TypeReference<CurrencyReadDto>() {
        });

        assertNotNull(currencyReadDto);
        assertEquals("840", currencyReadDto.getCode());
        assertEquals("USD", currencyReadDto.getCcy());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyReadDto));
    }

    @Test
    void failedTestGetByCode() throws Exception {

        mockMvc.perform(
                get("/api/v1/get-currency-by-code/{code}", "840")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("No Currency found with the provided code: 840", resolvedException.getMessage());
                })
                .andDo(print());

    }

    @Test
    void successfullyTestGetByCcy() throws Exception {
        currencyService.saveByOpenFeign();

        String contentAsString = mockMvc.perform(
                get("/api/v1/get-currency-by-ccy/{ccy}", "EUR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var currencyReadDto = objectMapper.readValue(contentAsString, new TypeReference<CurrencyReadDto>() {
        });

        assertNotNull(currencyReadDto);
        assertEquals("EUR", currencyReadDto.getCcy());
        assertEquals("978", currencyReadDto.getCode());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyReadDto));
    }

    @Test
    void failedTestGetByCcy() throws Exception {

        mockMvc.perform(
                get("/api/v1/get-currency-by-ccy/{ccy}", "EUR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("No Currency found with the provided ccy: EUR", resolvedException.getMessage());
                })
                .andDo(print());

    }

    @Test
    void successfullyTestGetByPage() throws Exception {
        currencyService.saveByOpenFeign();

        String contentAsString = mockMvc.perform(
                get("/api/v1/get-currencies-by-page")
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
        var currencyReadDtoPage = objectMapper.readValue(contentAsString, new TypeReference<Page<CurrencyReadDto>>() {
        });

        assertNotNull(currencyReadDtoPage);
        assertEquals(75, currencyReadDtoPage.getTotalElements());
        assertEquals(8, currencyReadDtoPage.getTotalPages());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyReadDtoPage));
    }

    @Test
    void failedTestGetByPage() throws Exception {

        mockMvc.perform(
                get("/api/v1/get-currencies-by-page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("Not found Currencies in database!", resolvedException.getMessage());
                })
                .andDo(print());
    }

    @Test
    void successfullyTestGetAll() throws Exception {
        currencyService.saveByOpenFeign();

        String contentAsString = mockMvc.perform(
                get("/api/v1/get-all-currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);

        var currencyReadDtoList = objectMapper.readValue(contentAsString, new TypeReference<List<CurrencyReadDto>>() {
        });

        assertNotNull(currencyReadDtoList);
        assertEquals(75, currencyReadDtoList.size());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyReadDtoList));
    }

    @Test
    void failedTestGetAll() throws Exception {

        mockMvc.perform(
                get("/api/v1/get-all-currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DataNotFoundException.class, exceptionResult.getResolvedException());
                    DataNotFoundException resolvedException = (DataNotFoundException) exceptionResult.getResolvedException();
                    assertEquals("Not found Currencies in database!", resolvedException.getMessage());
                })
                .andDo(print());

    }
}
