package kz.project.techway.api;

import kz.project.techway.dto.CurrencyConvertDTO;
import kz.project.techway.dto.CurrencyRateDTO;
import kz.project.techway.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public List<CurrencyRateDTO> getCurrencyHistory(@PathVariable String currencyType, @PathVariable String period) {
        return currencyRateService.getCurrencyHistory(currencyType, period);
    }

    @PostMapping("/convert")
    public Double convertCurrency(@RequestBody CurrencyConvertDTO convertDTO) {
        double result = currencyRateService.convertCurrency(convertDTO);
        return result;
    }
}
