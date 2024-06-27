package kz.project.techway.service.impl;

import kz.project.techway.dto.input.CurrencyConvertDTO;
import kz.project.techway.dto.input.CurrencyDTO;
import kz.project.techway.dto.output.CurrencyRateHistoryDTO;
import kz.project.techway.entity.ConversionHistory;
import kz.project.techway.entity.Currency;
import kz.project.techway.entity.CurrencyRate;
import kz.project.techway.enums.CurrencyEnum;
import kz.project.techway.enums.TimePeriodEnum;
import kz.project.techway.exceptions.InvalidCurrencyException;
import kz.project.techway.exceptions.InvalidPeriodException;
import kz.project.techway.mapper.ConversionHistoryMapper;
import kz.project.techway.mapper.CurrencyMapper;
import kz.project.techway.mapper.CurrencyRateMapper;
import kz.project.techway.repository.ConversionHistoryRepository;
import kz.project.techway.repository.CurrencyRateRepository;
import kz.project.techway.repository.CurrencyRepository;
import kz.project.techway.service.AuthService;
import kz.project.techway.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static kz.project.techway.enums.CurrencyEnum.KZT;
import static kz.project.techway.util.HalykBankApiUrl.BANK_API_URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private final CurrencyRateRepository currencyRateRepository;
    private final ConversionHistoryRepository conversionHistoryRepository;
    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRepository currencyRepository;
    private final ConversionHistoryMapper conversionHistoryMapper;
    private final CurrencyMapper currencyMapper;

    @Override
    public void updateCurrencyRates() {
        Arrays.stream(CurrencyEnum.values())
                .forEach(this::updateCurrencyRatesForCurrency);
    }

    private void updateCurrencyRatesForCurrency(CurrencyEnum currencyEnum) {
        String url = BANK_API_URL + currencyEnum.getCode() + "/" + TimePeriodEnum.WEEK.getPeriod();
        try {
            List<CurrencyRateHistoryDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CurrencyRateHistoryDTO>>() {
                    }
            ).getBody();

            if (Objects.nonNull(response)) {
                Currency currency = getOrCreateCurrency(currencyEnum);

                response.stream()
                        .filter(dto -> !currencyRateRepository.existsByCurrencyAndDateAt(currency, dto.getDateAt()))
                        .map(dto -> {
                            CurrencyRate rate = currencyRateMapper.toEntity(dto);
                            rate.setCurrency(currency);
                            rate.setDateAt(dto.getDateAt());
                            return rate;
                        })
                        .forEach(currencyRateRepository::save);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error occurred while fetching data from API: {}", e.getMessage());
        } catch (RestClientException e) {
            log.error("RestClientException occurred while fetching data from API: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching data from API: {}", e.getMessage());
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

    @Override
    public List<CurrencyRateHistoryDTO> getCurrencyHistory(String currencyType, String period) {
        TimePeriodEnum timePeriodEnum = TimePeriodEnum.valueOf(period.toUpperCase());
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(endDate, timePeriodEnum);

        Currency currency = currencyRepository.findByCode(currencyType.toUpperCase())
                .orElseThrow(() -> new InvalidCurrencyException("Invalid currency: " + currencyType));

        List<CurrencyRate> history = currencyRateRepository.findByCurrencyAndDateAtBetweenOrderByDateAtDesc(
                currency,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        return currencyRateMapper.toDtoList(history);
    }


    private LocalDate calculateStartDate(LocalDate endDate, TimePeriodEnum period) {
        return switch (period) {
            case DAY -> endDate.minusDays(1);
            case WEEK -> endDate.minusWeeks(1);
            case MONTH -> endDate.minusMonths(1);
            case YEAR -> endDate.minusYears(1);
            default -> throw new InvalidPeriodException("Invalid period: " + period);
        };
    }

    @Override
    public BigDecimal convertCurrency(CurrencyConvertDTO dto) {
        BigDecimal amount = dto.getAmount();
        CurrencyRate fromRate = getLatestRate(dto.getFrom());
        CurrencyRate toRate = getLatestRate(dto.getTo());

        BigDecimal resultAmount = calculateConvertedAmount(dto, amount, fromRate, toRate);

        saveConversionHistory(dto);
        return resultAmount;
    }

    private CurrencyRate getLatestRate(CurrencyDTO currencyCode) {
        return currencyRateRepository.findFirstByCurrencyOrderByDateAtDesc(
                currencyRepository.findByCode(currencyCode.getCode().toUpperCase())
                        .orElseThrow(() -> new InvalidCurrencyException("Invalid currency: " + currencyCode))
        );
    }

    private BigDecimal calculateConvertedAmount(CurrencyConvertDTO dto, BigDecimal amount, CurrencyRate fromRate, CurrencyRate toRate) {
        if (KZT.getCode().equalsIgnoreCase(dto.getFrom().getCode())) {
            return amount.divide(toRate.getValue(), RoundingMode.HALF_UP);
        } else if (KZT.getCode().equalsIgnoreCase(dto.getTo().getCode())) {
            return amount.multiply(fromRate.getValue());
        } else {
            BigDecimal amountInKZT = amount.multiply(fromRate.getValue());
            return amountInKZT.divide(toRate.getValue(), RoundingMode.HALF_UP);
        }
    }

    private void saveConversionHistory(CurrencyConvertDTO dto) {
        Currency fromCurrency = findOrCreateCurrency(dto.getFrom().getCode());
        Currency toCurrency = findOrCreateCurrency(dto.getTo().getCode());

        ConversionHistory history = new ConversionHistory();
        history.setUser(authService.getCurrentUser());
        history.setFromCurrency(fromCurrency);
        history.setToCurrency(toCurrency);
        history.setAmount(dto.getAmount());
        history.setConversionDateTime(LocalDateTime.now());

        conversionHistoryRepository.save(history);
    }

    private Currency findOrCreateCurrency(String code) {
        return currencyRepository.findByCode(code.toUpperCase())
                .orElseGet(() -> {
                    Currency newCurrency = new Currency();
                    newCurrency.setCode(code.toUpperCase());
                    return currencyRepository.save(newCurrency);
                });
    }

}
