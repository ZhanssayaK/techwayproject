package kz.project.techway.dto.output;

import kz.project.techway.dto.input.CurrencyDTO;
import kz.project.techway.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ConversionHistoryResponseDTO {
    private Long userId;
    private CurrencyDTO fromCurrency;
    private CurrencyDTO toCurrency;
    private BigDecimal amount;
    private LocalDateTime conversionDateTime;
}
