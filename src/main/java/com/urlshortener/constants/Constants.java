package com.urlshortener.constants;


import java.time.Duration;

public class Constants {
    private Constants(){}
    public static final String DOMAIN = "https://short.ly/";
    public static final Duration TTL = Duration.ofDays(30);
    public static final String REDIS_URL_ID_KEY = "url:id";
}
