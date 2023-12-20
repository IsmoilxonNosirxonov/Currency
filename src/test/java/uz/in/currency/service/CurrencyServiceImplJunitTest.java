package uz.in.currency.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.entity.Currency;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.service.currency.CurrencyServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplJunitTest {

    @Mock
    private CurrencyRepository currencyRepository;
    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Test
    public void testSave() {

        Mockito.when(currencyRepository.findAll()).thenReturn(List.of(new Currency()));
        Mockito.when(currencyRepository.save(any(Currency.class))).thenReturn(new Currency());

//        String result = currencyService.save();
//        assertEquals("Success", result);
    }

    @Test
    public void testGetByCode() {

        Mockito.when(currencyRepository.findByCode(anyString())).thenReturn(Optional.of(new Currency()));

        CurrencyReadDto result = currencyService.getByCode("840");
        assertNotNull(result);
    }

    @Test
    public void testGetByCodeDataNotFound(){

        Mockito.when(currencyRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class,()->currencyService.getByCode("940"));
    }

    @Test
    public void testGetByCcy() {

        Mockito.when(currencyRepository.findByCcy(anyString())).thenReturn(Optional.of(new Currency()));

        CurrencyReadDto result = currencyService.getByCcy("USD");
        assertNotNull(result);
    }

    @Test
    public void testGetByCcyDataNotFound(){

        Mockito.when(currencyRepository.findByCcy(anyString())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class,()->currencyService.getByCcy("EUR"));
    }

    @Test
    public void testGetByPage() {

        PageRequest pageable = PageRequest.of(0, 10);
        Page mockPage = Mockito.mock(Page.class);
        Mockito.when(currencyRepository.findAll(pageable)).thenReturn((Page) mockPage);
        Mockito.when(mockPage.map(Mockito.any())).thenReturn((Page<CurrencyReadDto>)mockPage);

        Page<CurrencyReadDto> result = currencyService.getByPage(pageable);
        assertNotNull(result);
    }

    @Test
    public void testGetAll() {

        Mockito.when(currencyRepository.findAll()).thenReturn(List.of(new Currency()));

        List<CurrencyReadDto> result = currencyService.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}
