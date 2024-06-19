package kz.project.techway.scheduler;

import kz.project.techway.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyRateScheduler {

    private final CurrencyRateService currencyRateService;

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduleCurrencyRateUpdate() {
        currencyRateService.updateCurrencyRates();
    }
}
