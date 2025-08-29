package com.urlshortener.service.impl;

import com.urlshortener.repository.RedisRepository;
import com.urlshortener.service.HashStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.urlshortener.constants.Constants.REDIS_URL_ID_KEY;

@Service
@AllArgsConstructor
public class Base62IdStrategy implements HashStrategy {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();
    private final RedisRepository redisRepository;

    @Override
    public String hashUrl(String url) {
        Long id = redisRepository.generateId(REDIS_URL_ID_KEY);
        return encode(id);
    }

    private static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET.charAt((int)(num % BASE)));
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
