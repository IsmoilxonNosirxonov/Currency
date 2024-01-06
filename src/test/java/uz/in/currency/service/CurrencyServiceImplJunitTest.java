package uz.in.currency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import uz.in.currency.config.ApplicationProperties;
import uz.in.currency.dto.CurrencyDTOFromCBU;
import uz.in.currency.dto.CurrencyDTOFromNBU;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.entity.Currency;
import uz.in.currency.exception.CommonException;
import uz.in.currency.mapper.CurrencyMapper;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.service.feign.CurrencyFromCBUFeignClient;
import uz.in.currency.service.feign.CurrencyFromNBUFeignClient;
import uz.in.currency.service.strategy.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CurrencyServiceImplJunitTest {

    @Autowired
    private CurrencyMapper currencyMapper;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private StrategyContainer strategyContainer;
    @Mock
    private CurrencyFromCBUFeignClient cbuFeignClient;
    @Mock
    private CurrencyFromNBUFeignClient nbuFeignClient;
    @MockBean
    private CurrencyStrategy currencyStrategy;
    private CurrencyServiceImpl currencyService;

    @Before
    public void setUp() {
        CBUStrategyService cbuStrategyService = new CBUStrategyService(cbuFeignClient);
        NBUStrategyService nbuStrategyService = new NBUStrategyService(applicationProperties, nbuFeignClient);
        UnknownStrategyService unknownStrategyService = new UnknownStrategyService();
        List<CurrencyStrategy> currencyStrategies = List.of(cbuStrategyService, nbuStrategyService, unknownStrategyService);

        strategyContainer = new StrategyContainer(currencyStrategies);
        currencyService = new CurrencyServiceImpl(currencyRepository, strategyContainer, currencyMapper);
    }

    @Test
    public void testSaveByResTemplateHasStrategy() {
        List<StandardCurrencyDTO> standardCurrencyDTOList = currencyService.saveByResTemplate("cbu");
        assertNotNull(standardCurrencyDTOList);
        assertFalse(standardCurrencyDTOList.isEmpty());
        assertEquals(75, standardCurrencyDTOList.size());
        assertEquals("840", standardCurrencyDTOList.get(0).getCode());
        assertEquals("USD", standardCurrencyDTOList.get(0).getCcy());
    }

    @Test
    public void testSaveByResTemplateHasNotStrategy() {
        CommonException commonException = assertThrows(CommonException.class, () -> currencyService.saveByResTemplate("bu"));
        assertEquals("This bank was not found in the database!", commonException.getMessage());
    }

    @Test
    public void testSaveByOpenFeignHasStrategy() {
        CurrencyDTOFromNBU currencyDTOFromNBU = new CurrencyDTOFromNBU("12300.49", "12380.49", "840", "USD", "AQSH dollari", "12377.49", "18.12.2023");

        when(nbuFeignClient.getCurrencies()).thenReturn(List.of(currencyDTOFromNBU));

        List<StandardCurrencyDTO> standardCurrencyDTOList = currencyService.saveByOpenFeign("nbu");
        assertNotNull(standardCurrencyDTOList);
        assertFalse(standardCurrencyDTOList.isEmpty());
        assertEquals(1, standardCurrencyDTOList.size());
        assertEquals("840", standardCurrencyDTOList.get(0).getCode());
        assertEquals("USD", standardCurrencyDTOList.get(0).getCcy());
    }

    @Test
    public void testSaveByOpenFeignHasNotStrategy() {
        CommonException commonException = assertThrows(CommonException.class, () -> currencyService.saveByOpenFeign("bu"));
        assertEquals("This bank was not found in the database!", commonException.getMessage());
    }

    @Test
    public void testSaveCurrenciesHasDtoList() {
        List<StandardCurrencyDTO> dtoList = currencyService.saveByResTemplate("nbu");
        List<StandardCurrencyDTO> standardCurrencyDTOList = currencyService.saveCurrencies("nbu", dtoList);
        assertNotNull(standardCurrencyDTOList);
        assertFalse(standardCurrencyDTOList.isEmpty());
        assertEquals(24, standardCurrencyDTOList.size());
        assertEquals("784", standardCurrencyDTOList.get(0).getCode());
        assertEquals("AED", standardCurrencyDTOList.get(0).getCcy());
    }

    @Test
    public void testSaveCurrenciesHasNotDtoList() {
        CommonException commonException = assertThrows(CommonException.class, () -> currencyService.saveCurrencies("nbu", Collections.emptyList()));
        assertEquals("No currency found with provided bank: nbu", commonException.getMessage());
    }

    @Test
    public void testGetByCodeHasCurrency() {
        Currency currency = Currency.builder().code(840L).ccy("USD").ccyNm_UZ("AQSH dollari").rate(12377.49).date(LocalDate.now()).build();
        currencyRepository.save(currency);

        StandardCurrencyDTO byCode = currencyService.getByCode(840L);
        assertNotNull(byCode);
        assertEquals("USD", byCode.getCcy());
        assertEquals("AQSH dollari", byCode.getCcyNm_UZ());
        assertEquals("12377.49", byCode.getRate());
    }

    @Test
    public void testGetByCodeHasNotCurrency() {
        CommonException commonException = assertThrows(CommonException.class, () -> currencyService.getByCode(840L));
        assertEquals("No Currency found with the provided code: 840", commonException.getMessage());

    }

    @Test
    public void testGetByCcyHasCurrency() {
        Currency currency = Currency.builder().code(840L).ccy("USD").ccyNm_UZ("AQSH dollari").rate(12377.49).date(LocalDate.now()).build();
        currencyRepository.save(currency);

        StandardCurrencyDTO byCcy = currencyService.getByCcy("USD");
        assertNotNull(byCcy);
        assertEquals("840", byCcy.getCode());
        assertEquals("AQSH dollari", byCcy.getCcyNm_UZ());
        assertEquals("12377.49", byCcy.getRate());
    }

    @Test
    public void testGetByCcyHasNotCurrency() {
        CommonException commonException = assertThrows(CommonException.class, () -> currencyService.getByCcy("USD"));
        assertEquals("No Currency found with the provided ccy: USD", commonException.getMessage());

    }

    @Test
    public void testGetByPageHasCurrency() {
        Currency currency1 = Currency.builder().code(840L).ccy("USD").ccyNm_UZ("AQSH dollari").rate(12377.49).date(LocalDate.now()).build();
        Currency currency2 = Currency.builder().code(978L).ccy("EUR").ccyNm_UZ("EVRO").rate(13566.97).date(LocalDate.now()).build();
        currencyRepository.save(currency1);
        currencyRepository.save(currency2);

        Pageable pageable = PageRequest.of(0, 2);
        Page<StandardCurrencyDTO> resultPage = currencyService.getByPage(pageable);

        assertNotNull(resultPage);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalPages());
        assertEquals(2, resultPage.getTotalElements());
        assertTrue(resultPage.getContent().stream().allMatch(Objects::nonNull));
    }

    @Test
    public void testGetByPageHasNotCurrency() {
        Pageable pageable = PageRequest.of(0, 2);

        CommonException commonException = assertThrows(CommonException.class, () -> currencyService.getByPage(pageable));
        assertEquals("No Currency found with the provided page", commonException.getMessage());
    }

    @Test
    public void testGetAllHasCurrencies() {
        Currency currency1 = Currency.builder().code(840L).ccy("USD").ccyNm_UZ("AQSH dollari").rate(12377.49).date(LocalDate.now()).build();
        Currency currency2 = Currency.builder().code(978L).ccy("EUR").ccyNm_UZ("EVRO").rate(13566.97).date(LocalDate.now()).build();
        currencyRepository.save(currency1);
        currencyRepository.save(currency2);

        List<StandardCurrencyDTO> dtoList = currencyService.getAll();

        assertNotNull(dtoList);
        assertFalse(dtoList.isEmpty());
        assertEquals(2, dtoList.size());
    }

    @Test
    public void testGetAllHasNotCurrencies() {
        CommonException commonException = assertThrows(CommonException.class, () -> currencyService.getAll());
        assertEquals("No Currency found in database", commonException.getMessage());
    }
}
