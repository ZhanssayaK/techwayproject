package kz.project.techway.service.impl;

import kz.project.techway.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate template;

    @Override
    public void saveRefreshToken(String username, String refreshToken, long expirationTime) {
        template.opsForValue().set(username, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }
}
