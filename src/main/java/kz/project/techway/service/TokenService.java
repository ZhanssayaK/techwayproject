package kz.project.techway.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface TokenService {
    String generateAndSaveToken(UserDetails userDetails);

    String generateRefreshToken(String token);

    void saveToken(String username, String token);

    Date extractExpiration(String token);

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    void evictToken(String token);

    boolean isTokenExpired(String token);

    long getRefreshTokenExpiration();

    Boolean isTokenNeedsRefresh(String token);

    Boolean exists(String username);
}
