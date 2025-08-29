package com.urlshortener.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@AllArgsConstructor
public class RedisRepositoryImpl implements RedisRepository{
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String shortUrl, String longUrl, Duration URL_TTL) {
        redisTemplate.opsForValue().set(shortUrl, longUrl, URL_TTL);
        redisTemplate.opsForValue().set("long:" + longUrl, shortUrl, URL_TTL);
    }
    @Override
    public String findByShortUrl(String shortUrl) {
        return redisTemplate.opsForValue().get(shortUrl);
    }
    @Override
    public String findByLongUrl(String longUrl) {
        return redisTemplate.opsForValue().get("long:" + longUrl);
    }
    @Override
    public boolean existsByShortUrl(String shortUrl) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("long:" + shortUrl));
    }

    @Override
    public void incrementClicks(String shortUrl) {
        redisTemplate.opsForValue().increment("clicks:" + shortUrl);
    }
}
