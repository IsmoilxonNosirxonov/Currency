package uz.in.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in.currency.dto.StandartCurrencyDTO;
import uz.in.currency.service.CurrencyService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class CurrencyController {

    private final CurrencyService currencyService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/save-all-currencies-by-rest-template/{bankName}")
    public ResponseEntity<List<?>> saveByResTemplate(@PathVariable String bankName) {
        return ResponseEntity.ok(currencyService.saveByResTemplate(bankName));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/save-all-currencies-by-open-feign/{bankName}")
    public ResponseEntity<List<?>> saveByOpenFeign(@PathVariable String bankName) {
        return ResponseEntity.ok(currencyService.saveByOpenFeign(bankName));
    }

    @GetMapping("/get-currency-by-code/{code}")
    public ResponseEntity<StandartCurrencyDTO> getByCode(@PathVariable Integer code) {
        return ResponseEntity.ok(currencyService.getByCode(code));
    }

    @GetMapping("/get-currency-by-ccy/{ccy}")
    public ResponseEntity<StandartCurrencyDTO> getByCcy(@PathVariable String ccy) {
        return ResponseEntity.ok(currencyService.getByCcy(ccy));
    }

    @GetMapping("/get-currencies-by-page")
    public ResponseEntity<Page<StandartCurrencyDTO>> getByPage(@RequestParam(name = "page", defaultValue = "0") int page,
                                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(currencyService.getByPage(PageRequest.of(page,size)));
    }

    @GetMapping("/get-all-currencies")
    public ResponseEntity<List<?>> getAll() {
        return ResponseEntity.ok(currencyService.getAll());
    }
}
