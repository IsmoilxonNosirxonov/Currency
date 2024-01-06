package uz.in.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = "uz.in.currency.config")
public class CurrencyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyApplication.class, args);
    }
}
