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
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.entity.User;
import uz.in.currency.exception.DataNotFoundException;
import uz.in.currency.exception.DublicateValueException;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.role.UserRole;
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
        UserDTO userCreateDto = new UserDTO("Test","test@example.com", "password", UserRole.USER);
        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userCreateDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        TokenDTO save = userService.save(userCreateDto);

        assertNotNull(save);
    }

    @Test
    public void testSaveDuplicateValue() {
        UserDTO userCreateDto = new UserDTO("Test","test@example.com", "password", UserRole.USER);
        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(DublicateValueException.class, () -> userService.save(userCreateDto));
    }

    @Test
    public void testSignIn() {
        SignInDTO signInDto = new SignInDTO("test@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(signInDto.getEmail())).thenReturn(Optional.of(new User()));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        TokenDTO tokenDTO = userService.signIn(signInDto);

        assertNotNull(tokenDTO);
        assertEquals("jwtToken",tokenDTO.getToken());
    }

    @Test
    public void testSignInDataNotFound() {
        SignInDTO signInDto = new SignInDTO("nonexistent@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DataNotFoundException("User not found"));

        assertThrows(DataNotFoundException.class, () -> userService.signIn(signInDto));
    }

}