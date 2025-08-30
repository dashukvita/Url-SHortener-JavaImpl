package com.urlshortener.service.hashgenerator;

import com.urlshortener.repository.CacheRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


@Service
@AllArgsConstructor
public class HashGeneratorImpl implements HashGenerator {
    private static final int MAX_ATTEMPTS = 10;
    private final CacheRepository<String, String> cache;

    @Override
    public String encode(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            byte[] shortBytes = Arrays.copyOf(digest, 4);

            String hash;
            int attempt = 0;

            do {
                if (attempt > 0) {
                    shortBytes[shortBytes.length - 1] ^= attempt;
                }
                hash = Base62.encode(shortBytes);
                attempt++;
                if (attempt > MAX_ATTEMPTS) {
                    throw new RuntimeException("Failed to generate unique short URL after \" + MAX_ATTEMPTS + \" attempts");
                }
            } while (cache.contains(hash));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
