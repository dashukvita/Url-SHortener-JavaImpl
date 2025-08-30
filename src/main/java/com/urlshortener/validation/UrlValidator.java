package com.urlshortener.validation;

import java.net.*;

public class UrlValidator {
    private UrlValidator() {
    }
    public static boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) return false;

        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) return false;

            String host = uri.getHost();
            if (host == null || host.isBlank()) return false;

            try {
                InetAddress address = InetAddress.getByName(host);
                if (address.isLoopbackAddress() || address.isAnyLocalAddress() || address.isSiteLocalAddress())
                    return false;
            } catch (UnknownHostException e) {
                return false;
            }

            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
