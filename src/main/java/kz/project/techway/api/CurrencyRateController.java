package kz.project.techway.api;

import kz.project.techway.dto.input.CurrencyConvertDTO;
import kz.project.techway.dto.output.CurrencyRateHistoryDTO;
import kz.project.techway.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @PostMapping("/convert")
    public BigDecimal convertCurrency(@RequestBody CurrencyConvertDTO convertDTO) {
        return currencyRateService.convertCurrency(convertDTO);
    }
}
