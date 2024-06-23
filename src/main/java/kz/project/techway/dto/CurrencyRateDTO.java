package kz.project.techway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CurrencyRateDTO {

    @JsonProperty("date_at")
    private LocalDateTime dateAt;
    private BigDecimal value;
    @JsonProperty("currency")
    private String currencyCode;
}
