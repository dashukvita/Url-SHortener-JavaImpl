package com.urlshortener.exception;

import com.urlshortener.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UrlValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleUrlAlreadyExistsException(UrlValidationException ex){
        log.error("URL validation error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponseDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidUrl(IllegalArgumentException ex) {
        log.error("Invalid URL: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(ex.getMessage()));
    }
}
