package kz.project.techway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}
