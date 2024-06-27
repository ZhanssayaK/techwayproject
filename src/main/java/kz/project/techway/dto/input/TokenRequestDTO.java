package kz.project.techway.dto.input;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDTO {
    private String refreshToken;
}
