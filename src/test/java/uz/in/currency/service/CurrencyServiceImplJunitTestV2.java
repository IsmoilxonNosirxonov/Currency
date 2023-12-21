package uz.in.currency.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uz.in.currency.domain.dto.CurrencyCreateDto;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.entity.Currency;
import uz.in.currency.domain.exception.DataNotFoundException;
import uz.in.currency.repository.CurrencyRepository;
import uz.in.currency.feign.CurrencyFeignClient;
import uz.in.currency.service.currency.CurrencyServiceImpl;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CurrencyServiceImplJunitTestV2 {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyFeignClient currencyFeignClient;
    @Mock
    private RestTemplate restTemplate;

    private CurrencyServiceImpl currencyService;

    private static final String CBU_URL = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";

    @Before
    public void setUp() {
        currencyService = new CurrencyServiceImpl(currencyRepository, currencyFeignClient);
    }

    @Test
    public void successfullyTestSaveByResTemplate() {

        CurrencyCreateDto[] currencyCreateDtos = new CurrencyCreateDto[1];
        currencyCreateDtos[0] = CurrencyCreateDto.builder()
                .id(21L)
                .code("978")
                .ccy("EUR")
                .ccyNm_RU("ЕBPO")
                .ccyNm_UZ("EVRO")
                .ccyNm_UZC("EBPO")
                .ccyNm_EN("EURO")
                .nominal("1")
                .rate("13566.97")
                .diff("91.77")
                .date("18.12.2023")
                .build();

        when(restTemplate.getForObject(CBU_URL, CurrencyCreateDto[].class)).thenReturn(currencyCreateDtos);

        String result = currencyService.saveByResTemplate();
        Assert.assertEquals("Success", result);
        Assert.assertEquals(currencyCreateDtos[0].getCode(), currencyRepository.findByCode("978").get().getCode());
    }

    @Test
    public void successfullyestSaveByOpenFeign() {

        CurrencyCreateDto[] currencyCreateDtos = new CurrencyCreateDto[1];
        currencyCreateDtos[0] = CurrencyCreateDto.builder()
                .id(69L)
                .code("840")
                .ccy("USD")
                .ccyNm_RU("Доллар США")
                .ccyNm_UZ("AQSH dollari")
                .ccyNm_UZC("АК,Ш Доллари")
                .ccyNm_EN("US Dollar")
                .nominal("1")
                .rate("12377.49")
                .diff("19.46")
                .date("18.12.2023")
                .build();

        when(currencyFeignClient.getCurrencies()).thenReturn(currencyCreateDtos);

        System.out.println(entityManager.getEntityManager());
        String result = currencyService.saveByOpenFeign();
        Assert.assertEquals("Success", result);
        Assert.assertEquals(currencyCreateDtos[0].getCode(), currencyRepository.findByCode("840").get().getCode());
    }

    @Test
    public void successfullyTestGetByCode() {

        Currency currency = Currency.builder()
                .id(69L)
                .code("840")
                .ccy("USD")
                .ccyNm_RU("Доллар США")
                .ccyNm_UZ("AQSH dollari")
                .ccyNm_UZC("АК,Ш Доллари")
                .ccyNm_EN("US Dollar")
                .nominal("1")
                .rate("12377.49")
                .diff("19.46")
                .date("18.12.2023")
                .build();

        entityManager.persist(currency);
        entityManager.flush();

        CurrencyReadDto currencyReadDto = currencyService.getByCode("840");

        assertEquals(currency.getCode(), currencyReadDto.getCode());
        assertEquals(currency.getCcy(), currencyReadDto.getCcy());
        assertEquals(currency.getCcyNm_RU(), currencyReadDto.getCcyNm_RU());
        assertEquals(currency.getCcyNm_UZ(), currencyReadDto.getCcyNm_UZ());
        assertEquals(currency.getCcyNm_UZC(), currencyReadDto.getCcyNm_UZC());
        assertEquals(currency.getCcyNm_EN(), currencyReadDto.getCcyNm_EN());
        assertEquals(currency.getNominal(), currencyReadDto.getNominal());
        assertEquals(currency.getRate(), currencyReadDto.getRate());
        assertEquals(currency.getRate(), currencyReadDto.getRate());
        assertEquals(currency.getDiff(), currencyReadDto.getDiff());
        assertEquals(currency.getDate(), currencyReadDto.getDate());
    }

    @Test
    public void failedTestGetByCode() {
        assertThrows(DataNotFoundException.class, () -> currencyService.getByCode("940"));
    }

    @Test
    public void successfullyTestGetByCcy() {
        Currency currency = Currency.builder()
                .id(69L)
                .code("840")
                .ccy("USD")
                .ccyNm_RU("Доллар США")
                .ccyNm_UZ("AQSH dollari")
                .ccyNm_UZC("АК,Ш Доллари")
                .ccyNm_EN("US Dollar")
                .nominal("1")
                .rate("12377.49")
                .diff("19.46")
                .date("18.12.2023")
                .build();

        entityManager.persist(currency);
        entityManager.flush();

        CurrencyReadDto currencyReadDto = currencyService.getByCcy("USD");

        assertEquals(currency.getCode(), currencyReadDto.getCode());
        assertEquals(currency.getCcy(), currencyReadDto.getCcy());
        assertEquals(currency.getCcyNm_RU(), currencyReadDto.getCcyNm_RU());
        assertEquals(currency.getCcyNm_UZ(), currencyReadDto.getCcyNm_UZ());
        assertEquals(currency.getCcyNm_UZC(), currencyReadDto.getCcyNm_UZC());
        assertEquals(currency.getCcyNm_EN(), currencyReadDto.getCcyNm_EN());
        assertEquals(currency.getNominal(), currencyReadDto.getNominal());
        assertEquals(currency.getRate(), currencyReadDto.getRate());
        assertEquals(currency.getRate(), currencyReadDto.getRate());
        assertEquals(currency.getDiff(), currencyReadDto.getDiff());
        assertEquals(currency.getDate(), currencyReadDto.getDate());
    }

    @Test
    public void failedTestGetByCcy() {

        assertThrows(DataNotFoundException.class, () -> currencyService.getByCcy("USD"));
    }

    @Test
    public void successfullyTestGetByPage() {

        Currency currency1 = Currency.builder()
                .id(69L)
                .code("840")
                .ccy("USD")
                .ccyNm_RU("Доллар США")
                .ccyNm_UZ("AQSH dollari")
                .ccyNm_UZC("АК,Ш Доллари")
                .ccyNm_EN("US Dollar")
                .nominal("1")
                .rate("12377.49")
                .diff("19.46")
                .date("18.12.2023")
                .build();
        Currency currency2 = Currency.builder()
                .id(21L)
                .code("978")
                .ccy("EUR")
                .ccyNm_RU("ЕBPO")
                .ccyNm_UZ("EVRO")
                .ccyNm_UZC("EBPO")
                .ccyNm_EN("EURO")
                .nominal("1")
                .rate("13566.97")
                .diff("91.77")
                .date("18.12.2023")
                .build();

        entityManager.persist(currency1);
        entityManager.persist(currency2);
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 2);
        Page<CurrencyReadDto> resultPage = currencyService.getByPage(pageable);

        Assert.assertTrue(resultPage.getContent().stream().allMatch(Objects::nonNull));
        Assert.assertEquals(2, resultPage.getTotalElements());
        Assert.assertEquals(1, resultPage.getTotalPages());
        Assert.assertEquals(2, resultPage.getNumberOfElements());
        Assert.assertEquals(0, resultPage.getNumber());
    }

    @Test
    public void failedTestGetByPage() {

        Pageable pageable = PageRequest.of(0, 2);
        assertThrows(DataNotFoundException.class, () -> currencyService.getByPage(pageable));
    }

    @Test
    public void successfullyTestGetAll() {

        Currency currency1 = Currency.builder()
                .id(69L)
                .code("840")
                .ccy("USD")
                .ccyNm_RU("Доллар США")
                .ccyNm_UZ("AQSH dollari")
                .ccyNm_UZC("АК,Ш Доллари")
                .ccyNm_EN("US Dollar")
                .nominal("1")
                .rate("12377.49")
                .diff("19.46")
                .date("18.12.2023")
                .build();
        Currency currency2 = Currency.builder()
                .id(21L)
                .code("978")
                .ccy("EUR")
                .ccyNm_RU("ЕBPO")
                .ccyNm_UZ("EVRO")
                .ccyNm_UZC("EBPO")
                .ccyNm_EN("EURO")
                .nominal("1")
                .rate("13566.97")
                .diff("91.77")
                .date("18.12.2023")
                .build();

        entityManager.persist(currency1);
        entityManager.persist(currency2);
        entityManager.flush();

        List<CurrencyReadDto> currencyReadDtos = currencyService.getAll();

        Assert.assertTrue(currencyReadDtos.stream().allMatch((Objects::nonNull)));
        Assert.assertFalse(currencyReadDtos.isEmpty());
        Assert.assertEquals(2, currencyReadDtos.size());
        Assert.assertEquals("840", currencyReadDtos.get(1).getCode());
        Assert.assertEquals("978", currencyReadDtos.get(0).getCode());
    }

    @Test
    public void failedTestGetAll() {

        assertThrows(DataNotFoundException.class, () -> currencyService.getAll());
    }
}
