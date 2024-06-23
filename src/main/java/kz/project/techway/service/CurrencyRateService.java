package kz.project.techway.service;

import kz.project.techway.dto.CurrencyConvertDTO;
import kz.project.techway.dto.CurrencyRateDTO;
import kz.project.techway.entity.ConversionHistory;
import kz.project.techway.entity.Currency;
import kz.project.techway.entity.CurrencyRate;
import kz.project.techway.enums.CurrencyEnum;
import kz.project.techway.enums.TimePeriodEnum;
import kz.project.techway.mapper.CurrencyRateMapper;
import kz.project.techway.repository.ConversionHistoryRepository;
import kz.project.techway.repository.CurrencyRateRepository;
import kz.project.techway.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;
    private final ConversionHistoryRepository conversionHistoryRepository;
    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRepository currencyRepository;

    private static final String BANK_API_URL = "https://halykbank.kz/api/exchangerates/";

    public void updateCurrencyRates() {
        for (CurrencyEnum currencyEnum : CurrencyEnum.values()) {
            updateCurrencyRatesForCurrency(currencyEnum);
        }
    }

    private void updateCurrencyRatesForCurrency(CurrencyEnum currencyEnum) {
        String url = BANK_API_URL + currencyEnum.getCode() + "/" + TimePeriodEnum.WEEK.getPeriod();
        CurrencyRateDTO[] response = restTemplate.getForObject(url, CurrencyRateDTO[].class);

        if (response != null) {
            Currency currency = getOrCreateCurrency(currencyEnum);

            for (CurrencyRateDTO bankCurrencyRateDTO : response) {
                LocalDateTime dateAt = bankCurrencyRateDTO.getDateAt();
                if (!currencyRateRepository.existsByCurrencyAndDateAt(currency, dateAt)) {
                    CurrencyRate newCurrencyRate = currencyRateMapper.toEntity(bankCurrencyRateDTO);
                    newCurrencyRate.setDateAt(dateAt);
                    newCurrencyRate.setCurrency(currency);
                    currencyRateRepository.save(newCurrencyRate);
                }
            }
        }
    }

    private Currency getOrCreateCurrency(CurrencyEnum currencyEnum) {
        return currencyRepository.findByCode(currencyEnum.name())
                .orElseGet(() -> {
                    Currency newCurrency = new Currency();
                    newCurrency.setCode(currencyEnum.name());
                    return currencyRepository.save(newCurrency);
                });
    }

    public List<CurrencyRateDTO> getCurrencyHistory(String currencyType, String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(endDate, period);

        Currency currency = currencyRepository.findByCode(currencyType.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency: " + currencyType));

        List<CurrencyRate> history = currencyRateRepository.findByCurrencyAndDateAtBetweenOrderByDateAtDesc(
                currency,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        return history.stream()
                .map(currencyRateMapper::toDto)
                .toList();
    }

    private LocalDate calculateStartDate(LocalDate endDate, String period) {
        return switch (period.toLowerCase()) {
            case "day" -> endDate.minusDays(1);
            case "week" -> endDate.minusWeeks(1);
            case "year" -> endDate.minusYears(1);
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        };
    }

    public double convertCurrency(CurrencyConvertDTO dto) {
        BigDecimal amount = dto.getAmount();
        CurrencyRate fromRate = getLatestRate(dto.getFrom());
        CurrencyRate toRate = getLatestRate(dto.getTo());

        BigDecimal resultAmount = calculateConvertedAmount(dto, amount, fromRate, toRate);

        saveConversionHistory(dto);

        return resultAmount.doubleValue();
    }

    private CurrencyRate getLatestRate(String currencyCode) {
        return currencyRateRepository.findFirstByCurrencyOrderByDateAtDesc(
                currencyRepository.findByCode(currencyCode.toUpperCase())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid currency: " + currencyCode))
        );
    }

    private BigDecimal calculateConvertedAmount(CurrencyConvertDTO dto, BigDecimal amount, CurrencyRate fromRate, CurrencyRate toRate) {
        if ("KZT".equalsIgnoreCase(dto.getFrom())) {
            return amount.divide(toRate.getValue(), BigDecimal.ROUND_HALF_UP);
        } else if ("KZT".equalsIgnoreCase(dto.getTo())) {
            return amount.multiply(fromRate.getValue());
        } else {
            BigDecimal amountInKZT = amount.multiply(fromRate.getValue());
            return amountInKZT.divide(toRate.getValue(), BigDecimal.ROUND_HALF_UP);
        }
    }

    private void saveConversionHistory(CurrencyConvertDTO dto) {
        ConversionHistory history = new ConversionHistory();
        history.setUser(authService.getCurrentUser());
        history.setFromCurrency(dto.getFrom());
        history.setToCurrency(dto.getTo());
        history.setAmount(dto.getAmount());
        history.setConversionDateTime(LocalDateTime.now());
        conversionHistoryRepository.save(history);
    }
}
