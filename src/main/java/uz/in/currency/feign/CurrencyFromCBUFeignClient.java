package uz.in.currency.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uz.in.currency.dto.CurrencyDTOFromCBU;


@FeignClient(name = "currencyFromCBUClient", url = "https://cbu.uz")
public interface CurrencyFromCBUFeignClient {

    @GetMapping("/uz/arkhiv-kursov-valyut/json/")
    CurrencyDTOFromCBU[] getCurrencies();
}
