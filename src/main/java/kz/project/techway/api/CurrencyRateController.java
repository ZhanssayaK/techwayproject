package kz.project.techway.api;

import kz.project.techway.dto.input.CurrencyConvertDTO;
import kz.project.techway.dto.output.CurrencyRateHistoryDTO;
import kz.project.techway.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currency-rates")
@RequiredArgsConstructor
public class CurrencyRateController {
    private final CurrencyRateService currencyRateService;

    @GetMapping("/update")
    public void updateCurrencyRates() {
        currencyRateService.updateCurrencyRates();
    }

    @GetMapping("/history/{currencyType}/{period}")
    public List<CurrencyRateHistoryDTO> getCurrencyHistory(@PathVariable String currencyType, @PathVariable String period) {
        return currencyRateService.getCurrencyHistory(currencyType, period);
    }

    @GetMapping("/history-grouped/{currencyType}/{period}")
    public Map<LocalDateTime, List<CurrencyRateHistoryDTO>> getCurrencyHistoryGroupedByDate(@PathVariable String currencyType, @PathVariable String period) {
        Map<LocalDateTime, List<CurrencyRateHistoryDTO>> groupedHistory = currencyRateService.getCurrencyHistoryGroupedByDate(currencyType, period);
        return groupedHistory;
    }

    @PostMapping("/convert")
    public BigDecimal convertCurrency(@RequestBody CurrencyConvertDTO convertDTO) {
        return currencyRateService.convertCurrency(convertDTO);
    }
}
