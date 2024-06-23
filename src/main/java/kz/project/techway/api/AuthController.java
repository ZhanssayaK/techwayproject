package kz.project.techway.api;

import jakarta.servlet.http.HttpServletRequest;
import kz.project.techway.dto.*;
import kz.project.techway.exceptions.TokenNotFoundException;
import kz.project.techway.exceptions.UserNotFound;
import kz.project.techway.service.AuthService;
import kz.project.techway.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws UserNotFound {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestBody TokenRequestDTO tokenRequestDTO) throws TokenNotFoundException {
        return authService.refreshToken(tokenRequestDTO.getRefreshToken());
    }
}
