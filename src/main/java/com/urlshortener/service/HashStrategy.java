package com.urlshortener.service;

public interface HashStrategy {
    String hashUrl(String url);
}
