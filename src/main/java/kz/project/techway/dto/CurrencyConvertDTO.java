package kz.project.techway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConvertDTO {

    private BigDecimal amount;
    private String from;
    private String to;
}
