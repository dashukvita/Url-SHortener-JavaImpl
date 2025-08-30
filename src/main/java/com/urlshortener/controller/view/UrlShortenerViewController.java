package com.urlshortener.controller.view;

import com.urlshortener.dto.ResponseDto;
import com.urlshortener.service.UrlShortener;
import com.urlshortener.validation.UrlValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<ResponseDto<String>> shortenUrlAjax(@RequestParam String originalUrl) {
        if (UrlValidator.isValidUrl(originalUrl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.error("Invalid url: " + originalUrl));
        }
        String shortUrl = urlShortener.shorten(originalUrl);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(shortUrl));
    }

    @PostMapping("/retrieve")
    @ResponseBody
    public ResponseEntity<ResponseDto<String>> retrieveUrlAjax(@RequestParam String shortUrl) {
        if (!shortUrl.startsWith(DOMAIN) || UrlValidator.isValidUrl(shortUrl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.error("Invalid url: " + shortUrl));
        }

        Optional<String> originalUrl = urlShortener.retrieve(shortUrl);
        return originalUrl
                .map(url -> ResponseEntity.ok(ResponseDto.success(url)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseDto.error("Short URL not found")));
    }
}
