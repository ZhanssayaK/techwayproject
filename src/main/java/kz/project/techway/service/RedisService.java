package kz.project.techway.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate template;

    public RedisService(StringRedisTemplate template) {
        this.template = template;
    }

    public void saveRefreshToken(String username, String refreshToken, long expirationTime) {
        template.opsForValue().set(username, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }
}
