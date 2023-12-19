package uz.in.currency.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uz.in.currency.domain.dto.CurrencyReadDto;
import uz.in.currency.domain.dto.UserCreateDto;
import uz.in.currency.domain.entity.Currency;
import uz.in.currency.domain.dto.CurrencyCreateDto;
import uz.in.currency.domain.entity.User;

@Mapper
public interface MyMapper {

    MyMapper INSTANCE= Mappers.getMapper(MyMapper.class);

    @Mapping(source = "fullName",target = "fullName")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "password",target = "password")
    @Mapping(source = "role",target = "role")
    User userCreateDtoToUser(UserCreateDto userCreateDto);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "code",target = "code")
    @Mapping(source = "ccy",target = "ccy")
    @Mapping(source = "ccyNm_RU",target = "ccyNm_RU")
    @Mapping(source = "ccyNm_UZ",target = "ccyNm_UZ")
    @Mapping(source = "ccyNm_UZC",target = "ccyNm_UZC")
    @Mapping(source = "ccyNm_EN",target = "ccyNm_EN")
    @Mapping(source = "nominal",target = "nominal")
    @Mapping(source = "rate",target = "rate")
    @Mapping(source = "diff",target = "diff")
    @Mapping(source = "date",target = "date")
    Currency currencyCreateDtoToCurrency(CurrencyCreateDto currencyCreateDto);

    @Mapping(source = "code",target = "code")
    @Mapping(source = "ccy",target = "ccy")
    @Mapping(source = "ccyNm_RU",target = "ccyNm_RU")
    @Mapping(source = "ccyNm_UZ",target = "ccyNm_UZ")
    @Mapping(source = "ccyNm_UZC",target = "ccyNm_UZC")
    @Mapping(source = "ccyNm_EN",target = "ccyNm_EN")
    @Mapping(source = "nominal",target = "nominal")
    @Mapping(source = "rate",target = "rate")
    @Mapping(source = "diff",target = "diff")
    @Mapping(source = "date",target = "date")
    CurrencyReadDto currencyToCurrencyReadDto(Currency currency);

    Logger logger= LoggerFactory.getLogger(MyMapper.class);
    static CurrencyReadDto toCurrencyReadDto(Currency currency) {
        logger.info("Entering toCurrencyReadDto() method");
        if(currency==null){
            logger.warn("Input currency is null, exiting CurrencyReadDto() method");
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
        logger.info("Exiting CurrencyReadDto() method");
        return currencyReadDto;
    }
}
