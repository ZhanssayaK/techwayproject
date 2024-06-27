package kz.project.techway.dto.output;

import kz.project.techway.enums.RoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private RoleEnum role;
}
