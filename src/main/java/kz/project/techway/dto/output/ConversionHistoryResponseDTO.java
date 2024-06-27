package kz.project.techway.dto.output;

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
    private User user;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private LocalDateTime conversionDateTime;
}
