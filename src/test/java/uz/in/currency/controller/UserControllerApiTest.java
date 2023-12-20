package uz.in.currency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.domain.exception.DublicateValueException;
import uz.in.currency.domain.response.AuthenticationResponse;
import uz.in.currency.domain.role.UserRole;
import uz.in.currency.service.user.UserService;
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
        UserCreateDto userCreateDto = new UserCreateDto("Test","test@example.com", "test", UserRole.USER);
        AuthenticationResponse response = new AuthenticationResponse("Test");

        when(userService.save(any(UserCreateDto.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> responseEntity=userController.signUp(userCreateDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void signUpNegativeTest1(){
        UserCreateDto userCreateDto = new UserCreateDto("Test","test@example.com", "test", UserRole.USER);

        when(userService.save(any(UserCreateDto.class))).thenThrow(DublicateValueException.class);

        assertThrows(DublicateValueException.class, ()->userController.signUp(userCreateDto));
    }

    @Test
    public void signUpPositiveTest2(){
        UserCreateDto userCreateDto = new UserCreateDto("Test","test@example.com", "test", UserRole.USER);
        AuthenticationResponse response = new AuthenticationResponse("validToken");

        when(userService.save(any(UserCreateDto.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> responseEntity=userController.signUp(userCreateDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("validToken", responseEntity.getBody().getToken());
    }

    @Test
    public void signUpNegativeTest2(){
        UserCreateDto userCreateDto = new UserCreateDto("Test","test@example.com", "", UserRole.USER);

        when(userService.save(any(UserCreateDto.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, ()->userController.signUp(userCreateDto));
    }

    @Test
    public void signInPositiveTest1(){
        SignInDto signInDto = new SignInDto("test@example.com", "test");
        AuthenticationResponse response = new AuthenticationResponse("Test");

        when(userService.signIn(any(SignInDto.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> responseEntity=userController.signIn(signInDto);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void signInNegativeTest1() {
        SignInDto signInDto = new SignInDto("test@example.com", "test");
        Mockito.when(userService.signIn(any(SignInDto.class))).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> userController.signIn(signInDto));
    }

    @Test
    public void signInPositiveTest2() {
        SignInDto signInDto = new SignInDto("test@example.com", "test");
        AuthenticationResponse mockedResponse = new AuthenticationResponse("validToken");
        Mockito.when(userService.signIn(any(SignInDto.class))).thenReturn(mockedResponse);

        ResponseEntity<AuthenticationResponse> responseEntity = userController.signIn(signInDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("validToken", responseEntity.getBody().getToken());
    }

    @Test
    void signInNegativeTest2() {
        SignInDto signInDto = new SignInDto("test@example.com", "");
        Mockito.when(userService.signIn(any(SignInDto.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> userController.signIn(signInDto));
    }

}
