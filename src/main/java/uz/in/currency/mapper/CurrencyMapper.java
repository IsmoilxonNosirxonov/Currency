package uz.in.currency.mapper;

import org.mapstruct.*;
import uz.in.currency.dto.StandartCurrencyDTO;
import uz.in.currency.entity.Currency;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    @Mapping(source = "code", target = "code")
    @Mapping(source = "rate", target = "rate")
    @Mapping(source = "date", target = "date",dateFormat = "dd.MM.yyyy")
    Currency toEntity(StandartCurrencyDTO standartCurrencyDTO);


    @Mapping(source = "code", target = "code")
    @Mapping(source = "rate",target = "rate")
    @Mapping(source = "date",target = "date",dateFormat = "dd.MM.yyyy")
    StandartCurrencyDTO toDTO(Currency currency);

    @Mapping(source = "code", target = "code")
    @Mapping(source = "rate",target = "rate")
    @Mapping(source = "date",target = "date",dateFormat = "dd.MM.yyyy")
    List<StandartCurrencyDTO> toDTO(List<Currency> currencies);

}
