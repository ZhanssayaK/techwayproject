package kz.project.techway.service;

import jakarta.servlet.http.HttpServletRequest;
import kz.project.techway.dto.*;
import kz.project.techway.entity.User;
import kz.project.techway.exceptions.UserNotFound;
import kz.project.techway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RedisService redisService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long tokenExpiration;

    public RegisterDTO register(RegisterRequest req) {
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .role(req.getRole())
                .password(req.getPassword())
                .build();

        User savedUser = userRepository.save(user);
        String token = saveToken(savedUser);
        String refreshToken=tokenService.refreshToken(token);

        redisService.saveRefreshToken(user.getUsername(), refreshToken, tokenExpiration);
        Date expirationDate=tokenService.extractExpiration(token);

        return new RegisterDTO(token,refreshToken,expirationDate);
    }

    public LoginDTO login(LoginRequest req) throws UserNotFound {
        return userRepository.findByUsername(req.getUsername())
                .map(user -> {
                    String token = saveToken(user);
                    String refreshToken = tokenService.refreshToken(token);
                    redisService.saveRefreshToken(user.getUsername(), refreshToken, tokenExpiration);
                    Date expirationDate=tokenService.extractExpiration(token);
                    return new LoginDTO(token, refreshToken,expirationDate);
                })
                .orElseThrow(() -> new UserNotFound("User not found"));
    }

    private String saveToken(User user) {
        String token = tokenService.buildToken(user);
        tokenService.save(
                user.getUsername(),
                Token.builder()
                        .creationDate(new Date())
                        .isValid(true)
                        .jwtExpiration(tokenService.extractExpiration(token))
                        .userId(user.getId())
                        .username(user.getUsername())
                        .value(token)
                        .build()
                        .toString()
        );
        return token;
    }

    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            tokenService.evictSingleCacheValue("tokenCache", tokenService.extractUsername(jwt));
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public String refreshToken(String refreshToken){
        String username=tokenService.extractUsername(refreshToken);
        User user=userRepository.findByUsername(username).orElseThrow(()-> new UserNotFound("User not found"));
        return saveToken(user);
    }
}
