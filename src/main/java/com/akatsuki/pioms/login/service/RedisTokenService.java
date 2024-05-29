package com.akatsuki.pioms.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(userId, refreshToken, 7, TimeUnit.DAYS);
    }

    public String getRefreshToken(String userId) {
        return (String) redisTemplate.opsForValue().get(userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }
}
