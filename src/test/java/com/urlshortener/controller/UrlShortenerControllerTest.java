package com.urlshortener.controller;

import com.urlshortener.exception.UrlValidationException;
import com.urlshortener.service.UrlShortener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerControllerTest {

    private UrlShortenerController controller;
    private UrlShortener mockService;

    @BeforeEach
    void setUp() {
        mockService = Mockito.mock(UrlShortener.class);
        controller = new UrlShortenerController(mockService);
    }

    @Test
    void testShortenUrl() {
        String longUrl = "https://example.com";
        String shortUrl = "http://short.ly/abc123";

        Mockito.when(mockService.shorten(longUrl)).thenReturn(shortUrl);

        ResponseEntity<String> response = controller.shortenUrl(longUrl);

        assertEquals(shortUrl, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testRetrieveUrl() {
        String shortUrl = "http://short.ly/abc123";
        String longUrl = "https://example.com";

        Mockito.when(mockService.retrieve(shortUrl)).thenReturn(Optional.of(longUrl));

        ResponseEntity<String> response = controller.retrieveUrl(shortUrl);

        assertEquals(longUrl, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRetrieveUrlNotFound() {
        String shortUrl = "http://short.ly/unknown";

        Mockito.when(mockService.retrieve(shortUrl)).thenReturn(Optional.empty());

        ResponseEntity<String> response = controller.retrieveUrl(shortUrl);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testShortenUrlInvalidValues() {
        // Настраиваем мок для выброса исключения на некорректные URL
        Mockito.when(mockService.shorten(Mockito.isNull()))
                .thenThrow(new UrlValidationException("URL is null"));
        Mockito.when(mockService.shorten(""))
                .thenThrow(new UrlValidationException("URL is empty"));
        Mockito.when(mockService.shorten("   "))
                .thenThrow(new UrlValidationException("URL is blank"));

        // Проверяем null
        assertThrows(UrlValidationException.class, () -> controller.shortenUrl(null));

        // Проверяем пустую строку
        assertThrows(UrlValidationException.class, () -> controller.shortenUrl(""));

        // Проверяем пробелы
        assertThrows(UrlValidationException.class, () -> controller.shortenUrl("   "));
    }

    @Test
    void testRetrieveUrlInvalidValues() {
        Mockito.when(mockService.retrieve(Mockito.isNull()))
                .thenThrow(new UrlValidationException("URL is null"));
        Mockito.when(mockService.retrieve(""))
                .thenThrow(new UrlValidationException("URL is empty"));
        Mockito.when(mockService.retrieve("   "))
                .thenThrow(new UrlValidationException("URL is blank"));

        assertThrows(UrlValidationException.class, () -> controller.retrieveUrl(null));
        assertThrows(UrlValidationException.class, () -> controller.retrieveUrl(""));
        assertThrows(UrlValidationException.class, () -> controller.retrieveUrl("   "));
    }
}