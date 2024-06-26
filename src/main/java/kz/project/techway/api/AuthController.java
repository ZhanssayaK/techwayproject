package kz.project.techway.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kz.project.techway.dto.*;
import kz.project.techway.exceptions.TokenNotFoundException;
import kz.project.techway.exceptions.UserNotFound;
import kz.project.techway.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterDTO register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginRequest loginRequest) throws UserNotFound {
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestBody TokenRequestDTO tokenRequestDTO) throws TokenNotFoundException {
        return authService.refreshToken(tokenRequestDTO.getRefreshToken());
    }
}
