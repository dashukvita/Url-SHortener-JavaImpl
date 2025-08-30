package com.urlshortener.service.impl;

import com.urlshortener.aspect.Loggable;
import com.urlshortener.model.UrlDocument;
import com.urlshortener.repository.CacheRepository;
import com.urlshortener.repository.StorageRepository;
import com.urlshortener.service.HashStrategy;
import com.urlshortener.service.UrlShortener;
import com.urlshortener.validation.UrlValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static com.urlshortener.constants.Constants.*;

@Service
@AllArgsConstructor
public class UrlShortenerImpl implements UrlShortener {
    private final HashStrategy strategy;
    private final CacheRepository<String, String> cache;
    private final StorageRepository storage;

    @Override
    @Loggable
    public String shorten(String longUrl) {
        UrlValidation.validate(longUrl);

        String cachedShortUrl = String.valueOf(cache.getByValue(longUrl));
        if (cachedShortUrl != null) {
            return DOMAIN + cachedShortUrl;
        }

        String shortUrl;
        do {
            shortUrl = strategy.hashUrl(longUrl);
        } while (cache.contains(shortUrl));

        UrlDocument doc = new UrlDocument();
        doc.setShortUrl(shortUrl);
        doc.setLongUrl(longUrl);
        doc.setCreatedAt(Instant.now());

        storage.save(doc);
        cache.save(shortUrl, longUrl, TTL);
        return DOMAIN + shortUrl;
    }

    @Override
    @Loggable
    public Optional<String> retrieve(String shortUrl) {
        UrlValidation.validate(shortUrl);

        if (!shortUrl.startsWith(DOMAIN)) {
            return Optional.empty();
        }
        String key = shortUrl.substring(DOMAIN.length());

        String longUrl = cache.get(key);

        if (longUrl == null) {
            UrlDocument doc = storage.findUrlDocumentByShortUrl(key);
            if (doc != null) {
                longUrl = doc.getLongUrl();
                cache.save(key, longUrl, TTL);
            }
        } else {
            cache.incrementCounter(key);
        }
        return Optional.ofNullable(longUrl);
    }
}
