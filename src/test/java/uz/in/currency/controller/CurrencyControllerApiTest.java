package uz.in.currency.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.exception.CurrencyNotSaveException;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.service.currency.CurrencyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CurrencyControllerApiTest {

    private CurrencyService currencyService;
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        currencyService = Mockito.mock(CurrencyService.class);
        currencyController = new CurrencyController(currencyService);
    }

    @Test
    public void saveByResTemplatePositiveTest1() {
        when(currencyService.saveByResTemplate()).thenReturn("Success");

        ResponseEntity<String> responseEntity = currencyController.saveByResTemplate();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void saveByResTemplateNegativeTest1() {
        when(currencyService.saveByResTemplate()).thenThrow(CurrencyNotSaveException.class);

        currencyController.saveByResTemplate();

        assertThrows(CurrencyNotSaveException.class, () -> currencyController.saveByResTemplate());
    }

    @Test
    public void saveByOpenFeignPositiveTest1() {
        when(currencyService.saveByOpenFeign()).thenReturn("Success");

        ResponseEntity<String> responseEntity = currencyController.saveByOpenFeign();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void saveByOpenFeignNegativeTest1() {
        when(currencyService.saveByOpenFeign()).thenThrow(CurrencyNotSaveException.class);

        ResponseEntity<String> responseEntity = currencyController.saveByOpenFeign();

        assertThrows(CurrencyNotSaveException.class, () -> currencyController.saveByOpenFeign());
    }

    @Test
    public void getByCodePositiveTest1() {
        CurrencyReadDto currencyReadDto = new CurrencyReadDto();

        when(currencyService.getByCode(anyString())).thenReturn(currencyReadDto);

        ResponseEntity<CurrencyReadDto> responseEntity = currencyController.getByCode("840");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void getByCodeNegativeTest1() {

        when(currencyService.getByCode(anyString())).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> currencyController.getByCode("841"));
    }

    @Test
    public void getByCodeNegativeTest2() {

        when(currencyService.getByCode(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> currencyController.getByCode(null));
    }

    @Test
    public void getByCcyPositiveTest1() {
        CurrencyReadDto currencyReadDto = new CurrencyReadDto();

        when(currencyService.getByCode(anyString())).thenReturn(currencyReadDto);

        ResponseEntity<CurrencyReadDto> responseEntity = currencyController.getByCode("USD");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void getByCcyNegativeTest1() {
        when(currencyService.getByCode(anyString())).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> currencyController.getByCode("USD"));
    }

    @Test
    public void getByCcyNegativeTest2() {
        when(currencyService.getByCode(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> currencyController.getByCode(null));
    }

    @Test
    public void getByPagePositiveTest1() {
        List<CurrencyReadDto> currencyReadDtos = List.of(new CurrencyReadDto());

        Page<CurrencyReadDto> mockedPage = new PageImpl<>(currencyReadDtos);
        Mockito.when(currencyService.getByPage(any(PageRequest.class))).thenReturn(mockedPage);

        ResponseEntity<Page<CurrencyReadDto>> responseEntity = currencyController.getByPage(0, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getTotalElements());
    }

    @Test
    public void getByPageNegativeTest1() {
        Mockito.when(currencyService.getByPage(any(PageRequest.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> currencyController.getByPage(-1, 10));
    }

    @Test
    public void getByPageNegativeTest2() {
        Mockito.when(currencyService.getByPage(any(PageRequest.class))).thenThrow(DataNotFoundException.class);

        assertThrows(DataNotFoundException.class, () -> currencyController.getByPage(1, 10));
    }

    @Test
    public void getAllPositiveTest1() {
        List<CurrencyReadDto> mockedDtoList = List.of(new CurrencyReadDto());
        when(currencyService.getAll()).thenReturn(mockedDtoList);

        ResponseEntity<List<?>> responseEntity = currencyController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertTrue(responseEntity.getBody().get(0) instanceof CurrencyReadDto);
    }
}
