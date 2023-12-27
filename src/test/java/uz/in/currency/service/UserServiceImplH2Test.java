package uz.in.currency.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import uz.in.currency.config.JwtService;
import uz.in.currency.dto.SignInDTO;
import uz.in.currency.dto.TokenDTO;
import uz.in.currency.dto.UserDTO;
import uz.in.currency.entity.User;
import uz.in.currency.exception.DataNotFoundException;
import uz.in.currency.exception.DublicateValueException;
import uz.in.currency.mapper.UserMapper;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.role.UserRole;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserServiceImplH2Test {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;
    private UserServiceImpl userService;

    @Before
    public void setUp(){
        userService=new UserServiceImpl(authenticationManager,passwordEncoder,userRepository,jwtService,userMapper);
    }

    @Test
    public void successfullyTestSave(){
        UserDTO userCreateDto= UserDTO.builder()
                .fullName("Test")
                .email("test@gmail.com")
                .password("test")
                .role(UserRole.USER)
                .build();
        User user=User.builder()
                .fullName("Test")
                .email("test@gmail.com")
                .password("test")
                .role(UserRole.USER)
                .build();

        when(passwordEncoder.encode(userCreateDto.getPassword())).thenReturn("encode");
        when(userMapper.userDtoToUser(any(UserDTO.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        TokenDTO response = userService.save(userCreateDto);
        Assert.assertEquals("token",response.getToken());
        Assert.assertEquals(user.getFullName(),userRepository.findByEmail("test@gmail.com").get().getFullName());
        Assert.assertEquals(user.getRole(),userRepository.findByEmail("test@gmail.com").get().getRole());
    }

    @Test
    public void testSaveDuplicateValueException(){
        UserDTO userCreateDto= UserDTO.builder()
                .fullName("Test")
                .email("test@gmail.com")
                .password("")
                .role(UserRole.USER)
                .build();
        User user=User.builder()
                .fullName("Test")
                .email("test@gmail.com")
                .password("test")
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        Assert.assertThrows(DublicateValueException.class, () -> userService.save(userCreateDto));
    }

    @Test
    public void testSaveIllegalArgumentException(){
        UserDTO userCreateDto= UserDTO.builder()
                .fullName("Test")
                .email("test@gmail.com")
                .password("")
                .role(UserRole.USER)
                .build();
        User user=User.builder()
                .fullName("Test")
                .email("test@gmail.com")
                .password("")
                .role(UserRole.USER)
                .build();

        Assert.assertThrows(IllegalArgumentException.class, () -> userService.save(userCreateDto));
    }

    @Test
    public void testSignIn(){
        SignInDTO signInDto= SignInDTO.builder()
                .email("test2@gmail.com")
                .password("test")
                .build();
        User user=User.builder()
                .fullName("Test2")
                .email("test2@gmail.com")
                .password("test2")
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        TokenDTO response = userService.signIn(signInDto);
        Assert.assertEquals("jwtToken",response.getToken());
        Assert.assertEquals(user.getFullName(),userRepository.findByEmail("test2@gmail.com").get().getFullName());
        Assert.assertEquals(user.getRole(),userRepository.findByEmail("test2@gmail.com").get().getRole());
    }

    @Test
    public void testSaveDataNotFoundException(){
        SignInDTO signInDto= SignInDTO.builder()
                .email("test2@gmail.com")
                .password("test")
                .build();
        User user=User.builder()
                .fullName("Test2")
                .email("test2@gmail.com")
                .password("test2")
                .role(UserRole.USER)
                .build();

        Assert.assertThrows(DataNotFoundException.class, () -> userService.signIn(signInDto));
    }
}
