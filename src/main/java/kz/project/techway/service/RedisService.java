package kz.project.techway.service;

public interface RedisService {
    void saveRefreshToken(String username, String refreshToken, long expirationTime);
}
