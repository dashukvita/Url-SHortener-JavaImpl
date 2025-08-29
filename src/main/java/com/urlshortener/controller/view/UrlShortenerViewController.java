package com.urlshortener.controller.view;

import com.urlshortener.service.UrlShortener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
    public String shortenUrlAjax(@RequestParam String longUrl) {
        return urlShortener.shorten(longUrl);
    }

    @PostMapping("/retrieve")
    @ResponseBody
    public String retrieveUrlAjax(@RequestParam String shortUrl) {
        return urlShortener.retrieve(shortUrl)
                .orElse("Short URL not found");
    }
}
