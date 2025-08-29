package com.urlshortener.service.impl;

import com.urlshortener.aspect.Loggable;
import com.urlshortener.model.UrlDocument;
import com.urlshortener.repository.RedisRepository;
import com.urlshortener.repository.UrlRepository;
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
    private final RedisRepository redisRepository;
    private final UrlRepository urlRepository;

    @Override
    @Loggable
    public String shorten(String longUrl) {
        UrlValidation.validate(longUrl);

        String cachedShortUrl = redisRepository.findByLongUrl(longUrl);
        if (cachedShortUrl != null) {
            return DOMAIN + cachedShortUrl;
        }

        String shortUrl;
        do {
            shortUrl = strategy.hashUrl(longUrl);
        } while (redisRepository.existsByShortUrl(shortUrl));

        UrlDocument doc = new UrlDocument();
        doc.setShortUrl(shortUrl);
        doc.setLongUrl(longUrl);
        doc.setCreatedAt(Instant.now());

        urlRepository.save(doc);
        redisRepository.save(shortUrl, longUrl, TTL);
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

        String longUrl = redisRepository.findByShortUrl(key);

        if (longUrl == null) {
            UrlDocument doc = urlRepository.findUrlDocumentByShortUrl(key);
            if (doc != null) {
                longUrl = doc.getLongUrl();
                redisRepository.save(key, longUrl, TTL);
            }
        } else {
            redisRepository.incrementClicks(key);
        }
        return Optional.ofNullable(longUrl);
    }
}
