package kz.project.techway.dto.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CurrencyRateHistoryDTO {
    @JsonProperty("date_at")
    private LocalDateTime dateAt;

    private BigDecimal value;

    @JsonProperty("currency")
    private String currencyCode;
}
