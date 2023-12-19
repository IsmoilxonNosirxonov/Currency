package uz.in.currency.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.in.currency.domain.entity.Currency;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

    Optional<Currency> findByCode(String code);

    Optional<Currency> findByCcy(String ccy);
}
