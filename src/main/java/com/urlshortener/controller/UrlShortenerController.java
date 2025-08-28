package com.urlshortener.controller;

import com.urlshortener.dto.AbstractResponse;
import com.urlshortener.dto.ErrorResponseDto;
import com.urlshortener.dto.ResponseDto;
import com.urlshortener.service.UrlShortener;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(
        name = "URL Shortener API",
        description = "API for shortening long URLs and retrieving their original versions."
)
@RequestMapping(produces ={MediaType.APPLICATION_JSON_VALUE})
public class UrlShortenerController {
    private final UrlShortener urlShortener;

    @PostMapping("/api/shorten")
    @Operation(summary = "Shorten a URL", description = "Returns a shortened version of the original URL")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "URL shortened successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid URL format")
    })
    public ResponseEntity<ResponseDto> shortenUrl(
            @RequestParam
            @Parameter(description = "Original URL to shorten", example = "https://example.com")
            String url
    ) {
        String shortUrl = urlShortener.shorten(url);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(shortUrl));
    }

    @GetMapping("/api/retrieve")
    @Operation(summary = "Retrieve original URL", description = "Returns the original URL for a given shortened URL")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Original URL found",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Short URL not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<AbstractResponse> retrieveUrl(
            @RequestParam("shortUrl")
            @Parameter(description = "Shortened URL", example = "http://short.ly/abc123")
            String shortUrl
    ) {
        Optional<String> originalUrl = urlShortener.retrieve(shortUrl);
        return originalUrl.<ResponseEntity<AbstractResponse>>map(s -> ResponseEntity.ok(new ResponseDto(s)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto("Short URL not found")));
    }
}
