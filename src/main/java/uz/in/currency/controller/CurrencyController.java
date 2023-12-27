package uz.in.currency.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.service.CurrencyService;
import uz.in.currency.service.UserServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class CurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);
    private final CurrencyService currencyService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/save-by-rest/{bankName}")
    public ResponseEntity<List<?>> saveByResTemplate(@PathVariable String bankName) {
        logger.info("Request to save by Rest Template with param: {}", bankName);
        List<StandardCurrencyDTO> standardCurrencyDTOList = currencyService.saveByResTemplate(bankName);
        logger.info("Response to save by Rest Template with list of objects: {}", standardCurrencyDTOList);
        return ResponseEntity.ok(standardCurrencyDTOList);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/save-by-feign/{bankName}")
    public ResponseEntity<List<?>> saveByOpenFeign(@PathVariable String bankName) {
        logger.info("Request to save by Open Feign with param: {}", bankName);
        List<StandardCurrencyDTO> standardCurrencyDTOList = currencyService.saveByOpenFeign(bankName);
        logger.info("Response to save by Open Feign with list of objects: {}", standardCurrencyDTOList);
        return ResponseEntity.ok(standardCurrencyDTOList);
    }

    @GetMapping("/get-by-code/{code}")
    public ResponseEntity<StandardCurrencyDTO> getByCode(@PathVariable Long code) {
        logger.info("Request to get currency with code: {}", code);
        StandardCurrencyDTO byCode = currencyService.getByCode(code);
        logger.info("Response to get currency with data: {}", byCode);
        return ResponseEntity.ok(byCode);
    }

    @GetMapping("/get-by-ccy/{ccy}")
    public ResponseEntity<StandardCurrencyDTO> getByCcy(@PathVariable String ccy) {
        logger.info("Request to get currency with ccy: {}", ccy);
        StandardCurrencyDTO byCcy = currencyService.getByCcy(ccy);
        logger.info("Response to get currency with data: {}", byCcy);
        return ResponseEntity.ok(byCcy);
    }

    @GetMapping("/get-by-page")
    public ResponseEntity<Page<StandardCurrencyDTO>> getByPage(Pageable page) {
        logger.info("Request to get currency with page: {}", page);
        Page<StandardCurrencyDTO> byPage = currencyService.getByPage(page);
        logger.info("Response to get currency with data: {}", byPage);
        return ResponseEntity.ok(byPage);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<?>> getAll() {
        logger.info("Request to get currencies");
        List<StandardCurrencyDTO> dtoList = currencyService.getAll();
        logger.info("Response to get currencies: {}", dtoList);
        return ResponseEntity.ok(dtoList);
    }
}
