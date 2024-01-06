package uz.in.currency.mapper;

import org.mapstruct.*;
import uz.in.currency.dto.CurrencyDTOFromCBU;
import uz.in.currency.dto.CurrencyDTOFromNBU;
import uz.in.currency.dto.StandardCurrencyDTO;
import uz.in.currency.entity.Currency;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    @Mapping(source = "code", target = "code")
    @Mapping(source = "rate", target = "rate")
    @Mapping(source = "date", target = "date",dateFormat = "dd.MM.yyyy")
    Currency toEntity(StandardCurrencyDTO standardCurrencyDTO);


    @Mapping(source = "code", target = "code")
    @Mapping(source = "rate",target = "rate")
    @Mapping(source = "date",target = "date",dateFormat = "dd.MM.yyyy")
    StandardCurrencyDTO toDTO(Currency currency);

    @Mapping(source = "code", target = "code")
    @Mapping(source = "rate",target = "rate")
    @Mapping(source = "date",target = "date",dateFormat = "dd.MM.yyyy")
    List<StandardCurrencyDTO> toDTO(List<Currency> currencies);

    @Mapping(source = "code", target = "code")
    @Mapping(source = "rate",target = "rate")
    @Mapping(source = "date",target = "date",dateFormat = "dd.MM.yyyy")
    List<Currency> toEntity(List<StandardCurrencyDTO> currencies);
}
