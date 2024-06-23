package kz.project.techway.repository;

import kz.project.techway.entity.Currency;
import kz.project.techway.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    List<CurrencyRate> findByCurrencyAndDateAtBetweenOrderByDateAtDesc(Currency currency, LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByCurrencyAndDateAt(Currency currency, LocalDateTime dateAt);

    CurrencyRate findFirstByCurrencyOrderByDateAtDesc(Currency currency);
}
