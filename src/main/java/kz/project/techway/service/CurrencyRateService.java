package kz.project.techway.service;

import kz.project.techway.dto.input.CurrencyConvertDTO;
import kz.project.techway.dto.output.CurrencyRateHistoryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CurrencyRateService {
    void updateCurrencyRates();

    // пагинация ??
    Map<LocalDateTime, List<CurrencyRateHistoryDTO>> getCurrencyHistoryGroupedByDate(String currencyType, String period);

    List<CurrencyRateHistoryDTO> getCurrencyHistory(String currencyType, String period);

    BigDecimal convertCurrency(CurrencyConvertDTO dto);
}