package com.urlshortener.service.impl;

import com.urlshortener.service.HashStrategy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDStrategyImpl implements HashStrategy {
    @Override
    public String hashUrl(String url) {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
