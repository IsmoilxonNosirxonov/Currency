package uz.in.currency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import uz.in.currency.config.JwtService;
import uz.in.currency.domain.entity.User;
import uz.in.currency.domain.role.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JwtServiceJunitTest {

    private static final String SECRET_KEY = "6C460F3BE0342BC7A968DDE410A571AEECC56BF4F90CB0786FFE8FC41D1D9093";

    private JwtService jwtService;

    @Before
    public void setUp(){
        jwtService=new JwtService();
    }

    @Test
    public void testGenerateTokenByUserdetails(){

        UserDetails userDetails=User.builder()
                .id(UUID.randomUUID())
                .fullName("Test")
                .email("test@gmail.com")
                .password("test")
                .role(UserRole.USER)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        String token = jwtService.generateToken(userDetails);
    }

    @Test
    public void testGenerateToken(){

    }
}
