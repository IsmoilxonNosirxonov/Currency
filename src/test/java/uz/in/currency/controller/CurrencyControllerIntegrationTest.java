package uz.in.currency.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uz.in.currency.config.JwtService;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.entity.User;
import uz.in.currency.exception.CommonException;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.enumeration.UserRole;
import uz.in.currency.service.CurrencyService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIntegrationTest {

    private static String jwtToken;
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
    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    public void setUp() {
        User testUser = new User(UUID.randomUUID(), "Test", "test@gmail.com", "test", UserRole.ADMIN, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(testUser);

        jwtToken = jwtService.generateToken(testUser);
    }

    @Test
    public void successfullyTestSaveByRestTemplate() throws Exception {

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/save-by-rest/{bankName}", "cbu").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertNotNull(contentAsString);
        var currencyDTOList = objectMapper.readValue(contentAsString, new TypeReference<List<StandardCurrencyDTO>>() {
        });
        assertNotNull(currencyDTOList);
        assertFalse(currencyDTOList.isEmpty());
        assertEquals(75, currencyDTOList.size());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyDTOList));
    }

    @Test
    public void failedTestSaveByRestTemplate() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/save-by-rest/{bankName}", "cbu")).andExpect(MockMvcResultMatchers.status().isForbidden()).andDo(print());
    }

    @Test
    public void successfullyTestSaveByOpenFeign() throws Exception {

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/save-by-feign/{bankName}", "nbu").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertNotNull(contentAsString);
        var currencyDTOList = objectMapper.readValue(contentAsString, new TypeReference<List<StandardCurrencyDTO>>() {
        });
        assertNotNull(currencyDTOList);
        assertFalse(currencyDTOList.isEmpty());
        assertEquals(24, currencyDTOList.size());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyDTOList));
    }

    @Test
    public void failedTestSaveByOpenFeign() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/save-by-feign/{bankName}", "nbu")).andExpect(MockMvcResultMatchers.status().isForbidden()).andDo(print());
    }

    @Test
    void successfullyTestGetByCode() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-by-code/{code}", "840").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertNotNull(contentAsString);
        var standartCurrencyDTO = objectMapper.readValue(contentAsString, new TypeReference<StandardCurrencyDTO>() {
        });

        assertNotNull(standartCurrencyDTO);
        assertEquals("840", standartCurrencyDTO.getCode());
        assertEquals("USD", standartCurrencyDTO.getCcy());
        assertEquals("AQSH dollari", standartCurrencyDTO.getCcyNm_UZ());
        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(standartCurrencyDTO));
    }

    @Test
    void failedTestGetByCode() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-by-code/{code}", "840").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andExpect(exceptionResult -> {
            assertNotNull(exceptionResult);
            assertInstanceOf(CommonException.class, exceptionResult.getResolvedException());
            CommonException resolvedException = (CommonException) exceptionResult.getResolvedException();
            assertEquals("No Currency found with the provided code: 840", resolvedException.getMessage());
        }).andDo(print());

    }

    @Test
    void successfullyTestGetByCcy() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-by-ccy/{ccy}", "EUR").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertNotNull(contentAsString);
        var standardCurrencyDTO = objectMapper.readValue(contentAsString, new TypeReference<StandardCurrencyDTO>() {
        });

        assertNotNull(standardCurrencyDTO);
        assertEquals("EUR", standardCurrencyDTO.getCcy());
        assertEquals("978", standardCurrencyDTO.getCode());
        assertEquals("EVRO", standardCurrencyDTO.getCcyNm_UZ());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(standardCurrencyDTO));
    }

    @Test
    void failedTestGetByCcy() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-by-ccy/{ccy}", "EUR").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andExpect(exceptionResult -> {
            assertNotNull(exceptionResult);
            assertInstanceOf(CommonException.class, exceptionResult.getResolvedException());
            CommonException resolvedException = (CommonException) exceptionResult.getResolvedException();
            assertEquals("No Currency found with the provided ccy: EUR", resolvedException.getMessage());
        }).andDo(print());

    }

    @Test
    void successfullyTestGetByPage() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-by-page?page0&size=10").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertNotNull(contentAsString);
        var dtoPage = objectMapper.readValue(contentAsString, new TypeReference<Page<StandardCurrencyDTO>>() {
        });

        assertNotNull(dtoPage);
        assertFalse(dtoPage.isEmpty());
        assertEquals(75, dtoPage.getTotalElements());
        assertEquals(8, dtoPage.getTotalPages());
        assertEquals(10, dtoPage.getSize());
        assertEquals(0, dtoPage.getNumber());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoPage));
    }

    @Test
    void failedTestGetByPage() throws Exception {
        currencyService.saveByOpenFeign("cbu");
        Pageable pageable = PageRequest.of(2, 100);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-by-page?page2&size=100").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken).param("page", "1").param("size", "10")).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andExpect(exceptionResult -> {
            assertNotNull(exceptionResult);
            assertInstanceOf(CommonException.class, exceptionResult.getResolvedException());
            CommonException resolvedException = (CommonException) exceptionResult.getResolvedException();
            assertEquals("No Currency found with the provided page", resolvedException.getMessage());
        }).andDo(print());
    }

    @Test
    void successfullyTestGetAll() throws Exception {
        currencyService.saveByOpenFeign("cbu");

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/api/get-all").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertNotNull(contentAsString);

        var dtoList = objectMapper.readValue(contentAsString, new TypeReference<List<StandardCurrencyDTO>>() {
        });

        assertNotNull(dtoList);
        assertEquals(75, dtoList.size());

        System.out.println("RESULT " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoList));
    }

    @Test
    void failedTestGetAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-all").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)).andExpect(MockMvcResultMatchers.status().isInternalServerError()).andExpect(exceptionResult -> {
            assertNotNull(exceptionResult);
            assertInstanceOf(CommonException.class, exceptionResult.getResolvedException());
            CommonException resolvedException = (CommonException) exceptionResult.getResolvedException();
            assertEquals("No Currency found in database", resolvedException.getMessage());
        }).andDo(print());

    }
}
