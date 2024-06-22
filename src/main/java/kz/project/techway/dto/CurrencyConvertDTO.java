package kz.project.techway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConvertDTO {
    private double amount;

    private String from;

    private String to;
}
