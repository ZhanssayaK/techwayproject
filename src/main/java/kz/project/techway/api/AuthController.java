package kz.project.techway.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kz.project.techway.dto.input.LoginRequestDTO;
import kz.project.techway.dto.input.RegisterRequestDTO;
import kz.project.techway.dto.input.TokenRequestDTO;
import kz.project.techway.dto.output.AuthDTO;
import kz.project.techway.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthDTO register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public AuthDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestBody TokenRequestDTO tokenRequestDTO) {
        return authService.refreshToken(tokenRequestDTO.getRefreshToken());
    }
}
