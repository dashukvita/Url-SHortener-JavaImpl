package com.urlshortener.repository;

import java.time.Duration;

public interface RedisRepository {
    void save(String shortUrl, String longUrl, Duration TTL);
    String findByShortUrl(String shortUrl);
    String findByLongUrl(String longUrl);
    boolean existsByShortUrl(String shortUrl);
    void incrementClicks(String shortUrl);
    Long generateId(String key);
}
