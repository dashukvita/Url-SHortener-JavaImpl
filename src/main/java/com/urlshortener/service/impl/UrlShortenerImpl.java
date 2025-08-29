package com.urlshortener.service.impl;

import com.urlshortener.repository.RedisRepository;
import com.urlshortener.service.HashStrategy;
import com.urlshortener.service.UrlShortener;
import com.urlshortener.validation.UrlValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UrlShortenerImpl implements UrlShortener {
    private final HashStrategy strategy;
    private final String DOMAIN = "http://short.ly/";

    private static final Duration URL_TTL = Duration.ofDays(30);

    private final RedisRepository redisRepository;

    @Override
    public String shorten(String longUrl) {
        UrlValidation.validate(longUrl);

        if (redisRepository.existsByLongUrl(longUrl)) {
            return DOMAIN + redisRepository.findByLongUrl(longUrl);
        }

        String key;
        synchronized (this) {
            do {
                key = strategy.hashUrl(longUrl);
            } while (redisRepository.existsByShortUrl(key));
            redisRepository.save(key, longUrl, URL_TTL);
        }
        return DOMAIN + key;
    }

    @Override
    public Optional<String> retrieve(String shortUrl) {
        UrlValidation.validate(shortUrl);

        if (!shortUrl.startsWith(DOMAIN)) {
            return Optional.empty();
        }
        String key = shortUrl.substring(DOMAIN.length());
        return Optional.ofNullable(redisRepository.findByShortUrl(key));
    }
}
