package kz.project.techway.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate template;

    public RedisService(StringRedisTemplate template) {
        this.template = template;
    }

    public void testConnection() {
        template.opsForValue().set("testKey", "testValue");
        String value = template.opsForValue().get("testKey");
        System.out.println("Value from Redis: " + value);
    }
}
