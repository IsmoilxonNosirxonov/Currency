package uz.in.currency.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uz.in.currency.domain.dto.CurrencyCreateDto;

@FeignClient(name = "currencyClient", url = "https://cbu.uz")
public interface CurrencyFeignClient {

    @GetMapping("/uz/arkhiv-kursov-valyut/json/")
    CurrencyCreateDto[] getCurrencies();
}
