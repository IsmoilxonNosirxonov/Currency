package uz.in.currency.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.exception.DublicateValueException;
import uz.in.currency.domain.response.AuthenticationResponse;
import uz.in.currency.domain.role.UserRole;
import uz.in.currency.service.user.UserService;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void successfullyTestSignUp() throws Exception {
        UserCreateDto createDto =new UserCreateDto("Test", "test@example.com", "password", UserRole.USER);

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8083/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);
        var response = objectMapper.readValue(contentAsString, new TypeReference<AuthenticationResponse>() {});

        assertNotNull(response);

        System.out.println("Result: "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    @Test
    public void failedTestSignUp() throws Exception {
        UserCreateDto createDto =new UserCreateDto("Test", "test@example.com", "password", UserRole.USER);
        userService.save(createDto);

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8083/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto))
                )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(DublicateValueException.class, exceptionResult.getResolvedException());
                    DublicateValueException resolvedException = (DublicateValueException) exceptionResult.getResolvedException();
                    assertEquals("This email already exists: " + createDto.getEmail(),resolvedException.getMessage());
                })
                .andDo(print());
    }

    @Test
    public void successfullyTestSignIn() throws Exception {
        SignInDto signInDto=new SignInDto("test@gmail.com", "test");
        UserCreateDto createDto =new UserCreateDto("Test", "test@gmail.com", "test", UserRole.USER);
        userService.save(createDto);

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8083/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(contentAsString);

        var response = objectMapper.readValue(contentAsString, new TypeReference<AuthenticationResponse>() {});

        assertNotNull(response);

        System.out.println("Result: "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    @Test
    public void failedTestSignIn() throws Exception {
        SignInDto signInDto=new SignInDto("test@gmail.com", "test");

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8083/api/v1/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto))
                )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(exceptionResult -> {
                    assertNotNull(exceptionResult);
                    assertInstanceOf(BadCredentialsException.class, exceptionResult.getResolvedException());
                    BadCredentialsException resolvedException = (BadCredentialsException) exceptionResult.getResolvedException();
                    assertEquals("Bad credentials",resolvedException.getMessage());
                })
                .andDo(print());

    }
}
