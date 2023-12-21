package uz.in.currency.service.currency;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.in.currency.domain.dto.CurrencyReadDto;

import java.util.List;

@Service
public interface CurrencyService {

    String saveByResTemplate();

    String saveByOpenFeign();

    CurrencyReadDto getByCode(String code);

    CurrencyReadDto getByCcy(String ccy);

    Page<CurrencyReadDto> getByPage(Pageable pageable);

    List<CurrencyReadDto> getAll();
}
