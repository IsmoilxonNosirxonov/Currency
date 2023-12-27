package uz.in.currency.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uz.in.currency.dto.StandardCurrencyDTO;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "currency")
@Data
public class ApplicationProperties {
    private Map<String, String> ccyCodes;
}
