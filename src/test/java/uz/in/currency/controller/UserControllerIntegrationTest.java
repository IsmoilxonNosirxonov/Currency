package uz.in.currency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.response.AuthenticationResponse;
import uz.in.currency.domain.role.UserRole;
import uz.in.currency.service.user.UserService;
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

    private static final String API = "http://localhost:8083/api/v1/auth";

    @Test
    public void testSignUp() throws Exception {

        UserCreateDto createDto =new UserCreateDto("Test", "test@example.com", "password", UserRole.USER);

        mockMvc.perform(MockMvcRequestBuilders.post(API+"/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andDo(print());

    }

    @Test
    public void testSignIn() throws Exception {

        SignInDto signInDto=new SignInDto("test@gmail.com", "test");
        UserCreateDto createDto =new UserCreateDto("Test", "test@gmail.com", "test", UserRole.USER);
        AuthenticationResponse save = userService.save(createDto);


        mockMvc.perform(MockMvcRequestBuilders.post(API + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andDo(print());
    }
}
