package kz.project.techway.api;

import kz.project.techway.dto.CurrencyConvertDTO;
import kz.project.techway.dto.CurrencyRateDTO;
import kz.project.techway.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency-rates")
@RequiredArgsConstructor
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

//    @GetMapping("/update")
//    public ResponseEntity<Void> updateCurrencyRates() {
//        currencyRateService.updateCurrencyRates();
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/history/{currencyType}/{period}")
    public ResponseEntity<List<CurrencyRateDTO>> getCurrencyHistory(@PathVariable String currencyType, @PathVariable String period) {
        List<CurrencyRateDTO> history = currencyRateService.getCurrencyHistory(currencyType, period);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/convert")
    public double convertCurrency(@RequestBody CurrencyConvertDTO convertDTO) {
        return currencyRateService.convertCurrency(convertDTO);
    }
}
