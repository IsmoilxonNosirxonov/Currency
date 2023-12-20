package uz.in.currency.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import uz.in.currency.config.JwtService;
import uz.in.currency.domain.dto.SignInDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.entity.User;
import uz.in.currency.domain.response.AuthenticationResponse;
import uz.in.currency.domain.role.UserRole;
import uz.in.currency.repository.UserRepository;
import uz.in.currency.mapper.MyMapper;
import uz.in.currency.service.user.UserServiceImpl;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceImplH2Test {

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MyMapper myMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    private UserServiceImpl userService;

    @Before
    public void setUp(){
        userService=new UserServiceImpl(userRepository,passwordEncoder,jwtService,authenticationManager);
    }

    @Test
    public void testSave(){

        UserCreateDto userCreateDto= UserCreateDto.builder()
                .fullName("Test")
                .email("test@gmail.com")
                .password("test")
                .role(UserRole.USER)
                .build();
        User user=User.builder()
                .id(UUID.randomUUID())
                .fullName("Test")
                .email("test@gmail.com")
                .password("test")
                .role(UserRole.USER)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        when(passwordEncoder.encode(userCreateDto.getPassword())).thenReturn("encode");
        when(myMapper.userCreateDtoToUser(any(UserCreateDto.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        AuthenticationResponse response = userService.save(userCreateDto);
        Assert.assertEquals("token",response.getToken());
        Assert.assertEquals(user.getFullName(),userRepository.findByEmail("test@gmail.com").get().getFullName());
    }

    @Test
    public void testSignIn(){

        SignInDto signInDto= SignInDto.builder()
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

        AuthenticationResponse response = userService.signIn(signInDto);
        Assert.assertEquals("jwtToken",response.getToken());
        Assert.assertEquals(user.getFullName(),userRepository.findByEmail("test2@gmail.com").get().getFullName());
    }
}
