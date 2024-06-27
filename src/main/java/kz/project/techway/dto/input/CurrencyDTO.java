package kz.project.techway.dto.input;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyDTO {
    private UUID id;
    private String code;
}
