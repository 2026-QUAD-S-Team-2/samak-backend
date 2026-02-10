package com.oaosis.samak.infra.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaosis.samak.infra.redis.exception.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oaosis.samak.infra.redis.exception.RedisErrorCode.TYPE_MISMATCH;

@RequiredArgsConstructor
@Component
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveValue(String key, Object value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public void saveValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> Optional<T> getValue(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return Optional.empty();
        }

        if (!type.isInstance(value)) {
            throw new IllegalStateException("Redis value type mismatch.");
        }

        return Optional.of(type.cast(value));
    }

    public <T> Optional<T> getAndDeleteValue(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return Optional.empty();
        }

        redisTemplate.delete(key);

        if (!type.isInstance(value)) {
            throw new RedisException(TYPE_MISMATCH);
        }

        return Optional.of(type.cast(value));
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}