package kz.project.techway.api;

import jakarta.servlet.http.HttpServletRequest;
import kz.project.techway.dto.LoginRequest;
import kz.project.techway.dto.RegisterRequest;
import kz.project.techway.dto.TokenRequestDTO;
import kz.project.techway.dto.TokenResponseDTO;
import kz.project.techway.exceptions.TokenNotFoundException;
import kz.project.techway.exceptions.UserNotFound;
import kz.project.techway.service.AuthService;
import kz.project.techway.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final TokenService tokenService;

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(service.register(req));
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest req) throws UserNotFound {
        return ResponseEntity.ok(service.login(req));
    }

    @PostMapping("/logout")
    ResponseEntity<?> logout(HttpServletRequest req) {
        service.logout(req);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody TokenRequestDTO tokenRequestDTO) throws TokenNotFoundException {
        String refreshToken = tokenRequestDTO.getRefreshToken();

        if (refreshToken == null) {
            throw new TokenNotFoundException("Refresh token not provided");
        }

        // Check if the refresh token is valid
        if (!tokenService.isValidRefreshToken(refreshToken)) {
            throw new TokenNotFoundException("Invalid refresh token");
        }

        // Refresh the token
        String newToken = tokenService.refreshToken(refreshToken);

        // Return the new token in the response
        return ResponseEntity.ok(new TokenResponseDTO(newToken,refreshToken));
    }
}