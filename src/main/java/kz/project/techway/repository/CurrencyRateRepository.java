package kz.project.techway.repository;

import kz.project.techway.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    List<CurrencyRate> findByCurrencyAndDateAtBetweenOrderByDateAtDesc(String currency, Timestamp startDate, Timestamp endDate);

    boolean existsByCurrencyAndDateAt(String currency, Timestamp dateAt);
}
