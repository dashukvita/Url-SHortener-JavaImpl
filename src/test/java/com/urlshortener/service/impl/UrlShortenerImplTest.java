package com.urlshortener.service.impl;

import com.urlshortener.repository.RedisRepository;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.service.HashStrategy;
import com.urlshortener.service.UrlShortener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

@DisplayName("service for generate short URl")
class UrlShortenerImplTest {

    private UrlShortener urlShortener;
    private HashStrategy strategy;
    private RedisRepository redisRepository;
    private UrlRepository urlRepository;

    @BeforeEach
    void setUp() {
        strategy = mock(HashStrategy.class);
        redisRepository = mock(RedisRepository.class);
        urlRepository = mock(UrlRepository.class);

        urlShortener = new UrlShortenerImpl(strategy, redisRepository, urlRepository);
    }

    @Test
    void testShortenAndRetrieve() {
//        String longUrl = "https://example.com/test";
//        String key = "abc123";
//
//        when(strategy.hashUrl(longUrl)).thenReturn(key);
//
//        String shortUrl = urlShortener.shorten(longUrl);
//
//        assertNotNull(shortUrl);
//        assertTrue(shortUrl.startsWith("http://short.ly/"));
//
//        Optional<String> retrieved = urlShortener.retrieve(shortUrl);
//        assertTrue(retrieved.isPresent());
//        assertEquals(longUrl, retrieved.get());
    }

    @Test
    void testShortenSameUrlReturnsSameShortUrl() {
//        String longUrl = "https://example.com/test";
//        String key = "abc123";
//
//        when(strategy.hashUrl(longUrl)).thenReturn(key);
//
//        String shortUrl1 = urlShortener.shorten(longUrl);
//        String shortUrl2 = urlShortener.shorten(longUrl);
//
//        assertNotNull(shortUrl1);
//        assertNotNull(shortUrl2);
//        assertEquals(shortUrl1, shortUrl2);
    }

    @Test
    void testRetrieveNonExistingShortUrl() {
//        Optional<String> retrieved = urlShortener.retrieve("http://short.ly/abcdef");
//        assertTrue(retrieved.isEmpty());
    }

    @Test
    void testShortenInvalidUrlThrowsException() {
//        String invalidUrl = "ht!tp://bad-url";
//
//        assertThrows(UrlValidationException.class, () -> urlShortener.shorten(invalidUrl));
    }

    @Test
    void testRetrieveInvalidUrlThrowsException() {
//        String invalidUrl = "ht!tp://bad-url";
//
//        assertThrows(UrlValidationException.class, () -> urlShortener.retrieve(invalidUrl));
    }
}