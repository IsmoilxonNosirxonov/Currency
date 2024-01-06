package uz.in.currency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import uz.in.currency.config.JwtService;
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.entity.User;
import uz.in.currency.exception.CommonException;
import uz.in.currency.mapper.UserMapper;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.enumeration.UserRole;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserServiceImplJunitTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(authenticationManager, passwordEncoder, userRepository, jwtService, userMapper);
    }

    @Test
    public void testSaveAlreadyHasNot() {
        UserDTO userCreateDto = UserDTO.builder().fullName("Test").email("test@gmail.com").password("test").role(UserRole.USER).build();
        User user = User.builder().fullName("Test").email("test@gmail.com").password("test").role(UserRole.USER).build();

        User save = userService.save(userCreateDto);
        assertNotNull(save);
        assertEquals(user.getFullName(), save.getFullName());
        assertEquals(user.getEmail(), save.getEmail());
        assertEquals(user.getRole(), save.getRole());
    }

    @Test
    public void testSaveAlreadyHas() {
        UserDTO userCreateDto = UserDTO.builder().fullName("Test").email("test@gmail.com").password("").role(UserRole.USER).build();
        User user = User.builder().fullName("Test").email("test@gmail.com").password("test").role(UserRole.USER).build();

        userRepository.save(user);
        CommonException commonException = assertThrows(CommonException.class, () -> userService.save(userCreateDto));
        assertEquals("This email already exists: test@gmail.com", commonException.getMessage());
    }

    @Test
    public void testSavePasswordHasCorrect() {
        UserDTO userCreateDto = UserDTO.builder().fullName("Test").email("test@gmail.com").password("test").role(UserRole.USER).build();
        User user = User.builder().fullName("Test").email("test@gmail.com").password("test").role(UserRole.USER).build();

        User save = userService.save(userCreateDto);
        assertNotNull(save);
        assertEquals(user.getFullName(), save.getFullName());
        assertEquals(user.getEmail(), save.getEmail());
        assertEquals(user.getRole(), save.getRole());
    }

    @Test
    public void testSavePasswordHasInCorrect() {
        UserDTO userCreateDto = UserDTO.builder().fullName("Test").email("test@gmail.com").password("").role(UserRole.USER).build();
        User user = User.builder().fullName("Test").email("test@gmail.com").password("").role(UserRole.USER).build();

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> userService.save(userCreateDto));
        assertEquals("Password can not be null or empty", illegalArgumentException.getMessage());
    }

    @Test
    public void successfullyTestSignIn() {
        SignInDTO signInDto = SignInDTO.builder().email("test@gmail.com").password("test").build();
        User user = User.builder().fullName("Test").email("test@gmail.com").password("test").role(UserRole.USER).build();

        userRepository.save(user);

        TokenDTO tokenDTO = userService.signIn(signInDto);

        assertNotNull(tokenDTO);
    }

    @Test
    public void testSaveDataNotFoundException() {
        SignInDTO signInDto = SignInDTO.builder().email("test2@gmail.com").password("test").build();

        CommonException commonException = assertThrows(CommonException.class, () -> userService.signIn(signInDto));
        assertEquals("User not found this email: test2@gmail.com", commonException.getMessage());
    }
}
