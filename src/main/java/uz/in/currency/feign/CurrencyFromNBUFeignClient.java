package uz.in.currency.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uz.in.currency.dto.CurrencyDTOFromNBU;

@FeignClient(name = "currencyFromNBUClient", url = "https://nbu.uz")
public interface CurrencyFromNBUFeignClient {
    @GetMapping("/uz/exchange-rates/json/")
    CurrencyDTOFromNBU[] getCurrencies();
}
