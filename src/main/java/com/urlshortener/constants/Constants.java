package com.urlshortener.constants;


import java.time.Duration;

public class Constants {
    private Constants(){}
    public static final String DOMAIN = "https://short.ly/";
    public static final Duration TTL = Duration.ofDays(30);

    public static final String SHORT_KEY_PREFIX = "short:";
    public static final String LONG_KEY_PREFIX = "long:";
    public static final String CLICK_KEY_PREFIX = "clicks:";
}
