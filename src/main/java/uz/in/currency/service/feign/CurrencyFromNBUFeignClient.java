package uz.in.currency.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uz.in.currency.dto.CurrencyDTOFromNBU;

import java.util.List;

@FeignClient(name = "currencyFromNBUClient", url = "https://nbu.uz")
public interface CurrencyFromNBUFeignClient {
    @GetMapping("/uz/exchange-rates/json/")
    List<CurrencyDTOFromNBU> getCurrencies();
}
