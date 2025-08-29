package com.urlshortener.constants;


import java.time.Duration;

public class Constants {
    private Constants(){}
    public static final String DOMAIN = "https://short.de/";
    public static final Duration URL_TTL = Duration.ofDays(30);
}
