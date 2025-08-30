package com.urlshortener.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@AllArgsConstructor
public class RedisRepository implements CacheRepository<String, String> {
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    public void save(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set("short:" + key, value, ttl);
        redisTemplate.opsForValue().set("long:" + value, key, ttl);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public String getByValue(String value) {
        return redisTemplate.opsForValue().get("long:" + value);
    }

    @Override
    public boolean contains(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public long incrementCounter(String key) {
        return redisTemplate.opsForValue().increment("clicks:" + key);
    }

    @Override
    public long generateId(String key) {
        return redisTemplate.opsForValue().increment(key);
    }
}
