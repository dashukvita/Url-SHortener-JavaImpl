package com.urlshortener.controller;

import com.urlshortener.service.UrlShortener;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UrlShortenerController {
    private final UrlShortener urlShortener;

    @PostMapping("/api/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String url) {
        String shortUrl = urlShortener.shorten(url);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shortUrl);
    }

    @GetMapping("/api/retrieve")
    public ResponseEntity<String> retrieveUrl(@RequestParam String shortUrl) {
        return urlShortener.retrieve(shortUrl)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
