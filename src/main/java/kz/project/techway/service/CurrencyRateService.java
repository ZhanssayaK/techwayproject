package kz.project.techway.service;

import kz.project.techway.dto.CurrencyConvertDTO;
import kz.project.techway.dto.CurrencyRateDTO;
import kz.project.techway.entity.ConversionHistory;
import kz.project.techway.entity.CurrencyRate;
import kz.project.techway.enums.CurrencyEnum;
import kz.project.techway.enums.TimePeriodEnum;
import kz.project.techway.mapper.CurrencyRateMapper;
import kz.project.techway.repository.ConversionHistoryRepository;
import kz.project.techway.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyRateService {

    private final CurrencyRateRepository currencyRateRepository;
    private final ConversionHistoryRepository conversionHistoryRepository;
    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final CurrencyRateMapper currencyRateMapper;

    private static final String BANK_API_URL = "https://halykbank.kz/api/exchangerates/";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public void updateCurrencyRates() {
        updateCurrencyRatesForUrl(BANK_API_URL + CurrencyEnum.USD.getCode() + "/" + TimePeriodEnum.WEEK.getPeriod(), CurrencyEnum.USD);
        updateCurrencyRatesForUrl(BANK_API_URL + CurrencyEnum.EUR.getCode() + "/" + TimePeriodEnum.WEEK.getPeriod(), CurrencyEnum.EUR);
        updateCurrencyRatesForUrl(BANK_API_URL + CurrencyEnum.RUB.getCode() + "/" + TimePeriodEnum.WEEK.getPeriod(), CurrencyEnum.RUB);
    }

    private void updateCurrencyRatesForUrl(String url, CurrencyEnum currencyEnum) {
        CurrencyRateDTO[] response = restTemplate.getForObject(url, CurrencyRateDTO[].class);
        if (response != null) {
            for (CurrencyRateDTO bankCurrencyRateDTO : response) {
                Timestamp dateAt = Timestamp.valueOf(bankCurrencyRateDTO.getDateAt().toLocalDateTime());

                if (!currencyRateRepository.existsByCurrencyAndDateAt(currencyEnum.name(), dateAt)) {
                    CurrencyRate newCurrencyRate = currencyRateMapper.toEntity(bankCurrencyRateDTO);
                    newCurrencyRate.setDateAt(dateAt);
                    currencyRateRepository.save(newCurrencyRate);
                }
            }
        }
    }

    public List<CurrencyRateDTO> getCurrencyHistory(String currencyType, String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period.toLowerCase()) {
            case "day":
                startDate = endDate.minusDays(1);
                break;
            case "week":
                startDate = endDate.minusWeeks(1);
                break;
            case "year":
                startDate = endDate.minusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        List<CurrencyRate> history = currencyRateRepository.findByCurrencyAndDateAtBetweenOrderByDateAtDesc(
                currencyType.toUpperCase(),
                Timestamp.valueOf(startDate.atStartOfDay()),
                Timestamp.valueOf(endDate.plusDays(1).atStartOfDay())
        );

        return history.stream()
                .map(currencyRateMapper::toDto)
                .toList();
    }

    public double convertCurrency(CurrencyConvertDTO dto) {
        double resultAmount;
        if (dto.getFrom().equalsIgnoreCase("KZT")) {
            CurrencyRate toRate = currencyRateRepository.findFirstByCurrencyOrderByDateAtDesc(dto.getTo());
            resultAmount= dto.getAmount() / toRate.getValue();
        } else if (dto.getTo().equalsIgnoreCase("KZT")) {
            CurrencyRate fromRate = currencyRateRepository.findFirstByCurrencyOrderByDateAtDesc(dto.getFrom());
            resultAmount= dto.getAmount() * fromRate.getValue();
        } else {
            CurrencyRate fromRate = currencyRateRepository.findFirstByCurrencyOrderByDateAtDesc(dto.getFrom());
            CurrencyRate toRate = currencyRateRepository.findFirstByCurrencyOrderByDateAtDesc(dto.getTo());
            double amountInKZT = dto.getAmount() * fromRate.getValue();
            resultAmount= amountInKZT / toRate.getValue();
        }

        ConversionHistory history = new ConversionHistory();
        history.setUser(authService.getCurrentUser());
        history.setFromCurrency(dto.getFrom());
        history.setToCurrency(dto.getTo());
        history.setAmount(dto.getAmount());
        history.setConversionDateTime(LocalDateTime.now());
        conversionHistoryRepository.save(history);

        return resultAmount;
    }
}
