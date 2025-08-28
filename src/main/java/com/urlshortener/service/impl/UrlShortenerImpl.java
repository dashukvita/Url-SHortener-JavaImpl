package com.urlshortener.service.impl;

import com.urlshortener.service.HashStrategy;
import com.urlshortener.service.UrlShortener;
import com.urlshortener.validation.UrlValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlShortenerImpl implements UrlShortener {
    private final HashStrategy strategy;
    private final String domain;
    private final Map<String, String> shortToLong = new ConcurrentHashMap<>();
    private final Map<String, String> longToShort = new ConcurrentHashMap<>();

    public UrlShortenerImpl(HashStrategy strategy,
                            @Value("${url.shortener.domain}") String domain) {
        this.strategy = strategy;
        this.domain = domain.endsWith("/") ? domain : domain + "/";
    }

    @Override
    public String shorten(String longUrl) {
        UrlValidation.validate(longUrl);

        if (longToShort.containsKey(longUrl)) {
            return domain + longToShort.get(longUrl);
        }

        String key;

        synchronized (this) {
            do {
                key = strategy.hashUrl(longUrl);
            } while (shortToLong.containsKey(key));

            shortToLong.put(key, longUrl);
            longToShort.put(longUrl, key);
        }
        return domain + key;
    }

    @Override
    public Optional<String> retrieve(String shortUrl) {
        UrlValidation.validate(shortUrl);

        if (!shortUrl.startsWith(domain)) {
            return Optional.empty();
        }

        String key = shortUrl.substring(domain.length());
        return Optional.ofNullable(shortToLong.get(key));
    }
}
