package com.emma.miniragent.idm.auth.token;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConditionalOnProperty(name = "auth.token-store", havingValue = "redis", matchIfMissing = true)
public class RedisTokenStore implements TokenStore {
    private static final String KEY_PREFIX = "auth:token";
    private final StringRedisTemplate redisTemplate;

    public RedisTokenStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String token, String username, long ttlSeconds) {
        this.redisTemplate.opsForValue().set(KEY_PREFIX + token, username,
                Duration.ofSeconds(ttlSeconds));
    }

    @Override
    public String getUsername(String token) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + token);
    }

    @Override
    public void remove(String token) {
        redisTemplate.delete(KEY_PREFIX + token);
    }
}
