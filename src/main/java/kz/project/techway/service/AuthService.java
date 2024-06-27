package kz.project.techway.service;

import jakarta.servlet.http.HttpServletRequest;
import kz.project.techway.dto.input.LoginRequestDTO;
import kz.project.techway.dto.input.RegisterRequestDTO;
import kz.project.techway.dto.output.AuthDTO;
import kz.project.techway.entity.User;

public interface AuthService {
    AuthDTO register(RegisterRequestDTO request);

    AuthDTO login(LoginRequestDTO req);

    void logout(HttpServletRequest request);

    User getCurrentUser();

    String refreshToken(String refreshToken);
}
