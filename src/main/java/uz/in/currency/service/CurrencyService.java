package uz.in.currency.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.in.currency.dto.StandardCurrencyDTO;

import java.util.List;


@Service
public interface CurrencyService {
    List<StandardCurrencyDTO> saveByResTemplate(String bankName);

    List<StandardCurrencyDTO> saveByOpenFeign(String bankName);

    StandardCurrencyDTO getByCode(Long code);

    StandardCurrencyDTO getByCcy(String ccy);

    Page<StandardCurrencyDTO> getByPage(Pageable pageable);

    List<StandardCurrencyDTO> getAll();
}
