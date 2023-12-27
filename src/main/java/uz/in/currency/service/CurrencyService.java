package uz.in.currency.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.in.currency.dto.StandartCurrencyDTO;
import java.util.List;


@Service
public interface CurrencyService {
    List<StandartCurrencyDTO> saveByResTemplate(String bankName);

    List<StandartCurrencyDTO> saveByOpenFeign(String bankName);

    StandartCurrencyDTO getByCode(Integer code);

    StandartCurrencyDTO getByCcy(String ccy);

    Page<StandartCurrencyDTO> getByPage(Pageable pageable);

    List<StandartCurrencyDTO> getAll();
}
