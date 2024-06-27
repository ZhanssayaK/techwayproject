package kz.project.techway.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CurrencyConvertDTO {
    @NotBlank(message = "Amount cannot be blank")
    private BigDecimal amount;

    @NotBlank(message = "Currency from cannot be blank")
    private CurrencyDTO from;

    @NotBlank(message = "Currency to cannot be blank")
    private CurrencyDTO to;
}
