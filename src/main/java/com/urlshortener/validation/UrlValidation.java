package com.urlshortener.validation;

import com.urlshortener.exception.UrlValidationException;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidation {
    public static void validate(String url) {
        if (url == null || url.isEmpty() || url.isBlank()) {
            throw new UrlValidationException("URL cannot be null or empty");
        }
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new UrlValidationException("Invalid URL format: " + url);
        }
    }
}
