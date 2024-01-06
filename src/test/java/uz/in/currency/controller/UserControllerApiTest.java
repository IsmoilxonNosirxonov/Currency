package uz.in.currency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.entity.User;
import uz.in.currency.enumeration.UserRole;
import uz.in.currency.exception.CommonException;
import uz.in.currency.service.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerApiTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp(){
        userService= Mockito.mock(UserService.class);
        userController=new UserController(userService);
    }

    @Test
    public void signUpPositiveTest1(){
        UserDTO userCreateDto = new UserDTO("Test","test@example.com", "test", UserRole.USER);
        User user=new User(UUID.randomUUID(),"Test","test@example.com", "test", UserRole.USER, LocalDateTime.now(),LocalDateTime.now());

        when(userService.save(any(UserDTO.class))).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.signUp(userCreateDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void signUpNegativeTest1(){
        UserDTO userCreateDto = new UserDTO("Test","test@example.com", "test", UserRole.USER);

        when(userService.save(any(UserDTO.class))).thenThrow(CommonException.class);

        assertThrows(CommonException.class, ()->userController.signUp(userCreateDto));
    }

    @Test
    public void signUpPositiveTest2(){
        UserDTO userCreateDto = new UserDTO("Test","test@example.com", "test", UserRole.USER);
        User user=new User(UUID.randomUUID(),"Test","test@example.com", "test", UserRole.USER, LocalDateTime.now(),LocalDateTime.now());

        when(userService.save(any(UserDTO.class))).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.signUp(userCreateDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void signUpNegativeTest2(){
        UserDTO userCreateDto = new UserDTO("Test","test@example.com", "", UserRole.USER);

        when(userService.save(any(UserDTO.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, ()->userController.signUp(userCreateDto));
    }

    @Test
    public void signInPositiveTest1(){
        SignInDTO signInDto = new SignInDTO("test@example.com", "test");
        TokenDTO response = new TokenDTO("Test");

        when(userService.signIn(any(SignInDTO.class))).thenReturn(response);

        ResponseEntity<TokenDTO> responseEntity=userController.signIn(signInDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void signInNegativeTest1() {
        SignInDTO signInDto = new SignInDTO("test@example.com", "test");
        Mockito.when(userService.signIn(any(SignInDTO.class))).thenThrow(CommonException.class);

        assertThrows(CommonException.class, () -> userController.signIn(signInDto));
    }

    @Test
    public void signInPositiveTest2() {
        SignInDTO signInDto = new SignInDTO("test@example.com", "test");
        TokenDTO mockedResponse = new TokenDTO("validToken");
        Mockito.when(userService.signIn(any(SignInDTO.class))).thenReturn(mockedResponse);

        ResponseEntity<TokenDTO> responseEntity = userController.signIn(signInDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("validToken", responseEntity.getBody().getToken());
    }

    @Test
    void signInNegativeTest2() {
        SignInDTO signInDto = new SignInDTO("test@example.com", "");
        Mockito.when(userService.signIn(any(SignInDTO.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> userController.signIn(signInDto));
    }

}
