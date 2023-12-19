package uz.in.currency.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.in.currency.config.JwtService;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.entity.User;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.domain.exception.DublicateValueException;
import uz.in.currency.domain.response.AuthenticationResponse;
import uz.in.currency.domain.role.UserRole;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.service.user.UserServiceImpl;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplJunitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSave() {

        UserCreateDto userCreateDto = new UserCreateDto("Test","test@example.com", "password", UserRole.USER);
        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userCreateDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        AuthenticationResponse authenticationResponse = userService.save(userCreateDto);

        assertNotNull(authenticationResponse);
    }

    @Test
    public void testSaveDuplicateValue() {

        UserCreateDto userCreateDto = new UserCreateDto("Test","test@example.com", "password", UserRole.USER);
        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DublicateValueException.class, () -> userService.save(userCreateDto));
    }

    @Test
    public void testSignIn() {

        SignInDto signInDto = new SignInDto("test@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(Optional.of(new User()));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse authenticationResponse = userService.signIn(signInDto);

        assertNotNull(authenticationResponse);
        assertEquals("jwtToken",authenticationResponse.getToken());
    }

    @Test
    public void testSignInDataNotFound() {

        SignInDto signInDto = new SignInDto("nonexistent@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DataNotFoundException("User not found"));

        assertThrows(DataNotFoundException.class, () -> userService.signIn(signInDto));
    }
}