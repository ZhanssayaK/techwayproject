package kz.project.techway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kz.project.techway.dto.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    private final RedisTemplate redisTemplate;

    private final CacheManager cacheManager;

    Logger logger = Logger.getLogger(AuthService.class.getName());

    @CachePut(key = "#userName", value = "tokenCache")
    public String save(String userName, String token) {
        return token;
    }

    public String get(String userName) {
        try {
            return cacheManager.getCache("tokenCache").get(userName).toString();
        } catch (NullPointerException e){
            logger.log(Level.SEVERE, "token not found for username: " + userName);
            return null;
        }
    }

    public Token getRefreshToken(String refreshToken) {
        return (Token) redisTemplate.opsForValue().get(refreshToken);
    }

    public void evictSingleCacheValue(String cacheName, String cacheKey) {
        try {
            cacheManager.getCache(cacheName).evict(cacheKey);
        } catch(NullPointerException e) {
            logger.info("Cache name key " + cacheKey + "not found for cache name " + cacheName);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String buildToken(
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String refreshToken(String token) {
        final Claims claims = extractAllClaims(token);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenNeedsRefresh(String token) {
        final Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        Date now = new Date();
        // Определяем, нужно ли обновлять токен на основе текущей даты и времени
        return expiration != null && now.after(expiration);
    }

    public boolean isValidRefreshToken(String refreshToken) {
        // Получаем информацию о токене из кэша или базы данных
        Token cachedToken = cacheManager.getCache("refreshTokenCache").get(refreshToken, Token.class);

        if (cachedToken == null) {
            return false;
        }

        if (cachedToken.getJwtExpiration().before(new Date())) {
            return false;
        }

        return true;
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}