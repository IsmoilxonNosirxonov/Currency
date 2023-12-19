package uz.in.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.service.currency.CurrencyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/save-all-currencies-by-rest-template")
    public ResponseEntity<String> saveByResTemplate() {
        return ResponseEntity.ok(currencyService.saveByResTemplate());
    }

    @GetMapping("/save-all-currencies-by-open-feign")
    public ResponseEntity<String> saveByOpenFeign() {
        return ResponseEntity.ok(currencyService.saveByOpenFeign());
    }

    @GetMapping("/get-currency-by-code/{code}")
    public ResponseEntity<CurrencyReadDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getByCode(code));
    }

    @GetMapping("/get-currency-by-ccy/{ccy}")
    public ResponseEntity<CurrencyReadDto> getByCcy(@PathVariable String ccy) {
        return ResponseEntity.ok(currencyService.getByCcy(ccy));
    }

    @GetMapping("/get-currencies-by-page")
    public ResponseEntity<Page<CurrencyReadDto>> getByPage(@RequestParam(name = "page", defaultValue = "0") int page,
                                                           @RequestParam(name = "size", defaultValue = "10") int size,
                                                           @RequestParam(name = "sortBy", defaultValue = "code") String sortBy) {
        return ResponseEntity.ok(currencyService.getByPage(PageRequest.of(page,size,Sort.by(sortBy))));
    }

    @GetMapping("/get-all-currencies")
    public ResponseEntity<List<?>> getAll() {
        return ResponseEntity.ok(currencyService.getAll());
    }
}
