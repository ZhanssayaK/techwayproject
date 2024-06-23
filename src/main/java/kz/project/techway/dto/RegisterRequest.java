package kz.project.techway.dto;

import kz.project.techway.enums.RoleEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private RoleEnum role;
}
