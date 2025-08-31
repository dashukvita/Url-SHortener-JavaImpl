package com.urlshortener.controller.view;

import com.urlshortener.service.UrlShortener;
import com.urlshortener.validation.UrlValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.urlshortener.constants.Constants.DOMAIN;


@Controller
@AllArgsConstructor
public class UrlShortenerViewController {
    private final UrlShortener urlShortener;


    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(required = false) String message) {
        model.addAttribute("message", message);
        return "url_form";
    }

    @PostMapping("/shorten")
    @ResponseBody
    public String shortenUrlAjax(@RequestParam String originalUrl) {
        if (UrlValidator.isNotValidUrl(originalUrl)) {
            return "Invalid url: " + originalUrl;
        }
        return urlShortener.shorten(originalUrl);
    }

    @PostMapping("/retrieve")
    @ResponseBody
    public String retrieveUrlAjax(@RequestParam String shortUrl) {
        if (!shortUrl.startsWith(DOMAIN)) {
            return "Invalid url: " + shortUrl;
        }

        return urlShortener.retrieve(shortUrl)
                .orElse("Short URL not found");
    }
}
