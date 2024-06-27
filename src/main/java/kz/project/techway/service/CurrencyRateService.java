package kz.project.techway.service;

import kz.project.techway.dto.input.CurrencyConvertDTO;
import kz.project.techway.dto.output.CurrencyRateHistoryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyRateService {
    void updateCurrencyRates();

    List<CurrencyRateHistoryDTO> getCurrencyHistory(String currencyType, String period);

    BigDecimal convertCurrency(CurrencyConvertDTO dto);
}