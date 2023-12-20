package uz.in.currency.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.entity.Currency;
import uz.in.currency.domain.dto.CurrencyCreateDto;
import uz.in.currency.domain.entity.User;

public class MyMapperImpl implements MyMapper{

    private static final Logger logger= LoggerFactory.getLogger(MyMapperImpl.class);
    @Override
    public User userCreateDtoToUser(UserCreateDto userCreateDto) {
        logger.info("Entering userCreateDtotoCurrency() method");
        if(userCreateDto == null){
            logger.warn("Input UserCreateDto is null, exiting userCreateDtoToUser() method");
            return null;
        }
        User user= User.builder()
                .fullName(userCreateDto.getFullName())
                .email(userCreateDto.getEmail())
                .password(userCreateDto.getPassword())
                .role(userCreateDto.getRole())
                .build();
        logger.info("Exiting userCreateDtotoCurrency() method");
        return user;
    }

    @Override
    public Currency currencyCreateDtoToCurrency(CurrencyCreateDto currencyCreateDto) {
        logger.info("Entering currencyCreateDtotoCurrency() method");
        if(currencyCreateDto==null){
            logger.warn("Input currencyCreateDto is null, exiting userCreateDtoToUser() method");
            return null;
        }
        Currency currency= Currency.builder()
                .id(currencyCreateDto.getId())
                .code(currencyCreateDto.getCode())
                .ccy(currencyCreateDto.getCcy())
                .ccyNm_RU(currencyCreateDto.getCcyNm_RU())
                .ccyNm_UZ(currencyCreateDto.getCcyNm_UZ())
                .ccyNm_UZC(currencyCreateDto.getCcyNm_UZC())
                .ccyNm_EN(currencyCreateDto.getCcyNm_EN())
                .nominal(currencyCreateDto.getNominal())
                .rate(currencyCreateDto.getRate())
                .diff(currencyCreateDto.getDiff())
                .date(currencyCreateDto.getDate())
                .build();
        logger.info("Exiting currencyCreateDtotoCurrency() method");
        return currency;
    }

    @Override
    public CurrencyReadDto currencyToCurrencyReadDto(Currency currency) {
        logger.info("Entering currencytoCurrencyReadDto() method");
        if(currency==null){
            logger.warn("Input currency is null, exiting currencytoCurrencyReadDto() method");
            return null;
        }
        CurrencyReadDto currencyReadDto= CurrencyReadDto.builder()
                .code(currency.getCode())
                .ccy(currency.getCcy())
                .ccyNm_RU(currency.getCcyNm_RU())
                .ccyNm_UZ(currency.getCcyNm_UZ())
                .ccyNm_UZC(currency.getCcyNm_UZC())
                .ccyNm_EN(currency.getCcyNm_EN())
                .nominal(currency.getNominal())
                .rate(currency.getRate())
                .diff(currency.getDiff())
                .date(currency.getDate())
                .build();
        logger.info("Exiting currencytoCurrencyReadDto() method");
        return currencyReadDto;
    }
}
