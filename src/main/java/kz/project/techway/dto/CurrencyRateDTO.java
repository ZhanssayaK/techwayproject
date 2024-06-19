package kz.project.techway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class CurrencyRateDTO {
    private Long date;

    @JsonProperty("date_at")
    private Timestamp dateAt;

    private Double value;

    private String currency;
}
