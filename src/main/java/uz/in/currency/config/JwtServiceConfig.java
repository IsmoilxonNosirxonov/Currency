package uz.in.currency.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtServiceConfig {

    @Value("${secret.key}")
    private String secretKey;

    @Bean
    public JwtService jwtService() {
        JwtService jwtService = new JwtService();
        jwtService.setSecretKey(secretKey);
        return jwtService;
    }
}
