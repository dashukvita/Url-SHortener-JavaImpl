package com.urlshortener.exception;

import com.urlshortener.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UrlValidationException.class)
    public ResponseEntity<ResponseDto> handleUrlAlreadyExistsException(UrlValidationException ex){
        log.error("URL validation error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ResponseDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto> handleInvalidUrl(IllegalArgumentException ex) {
        log.error("Invalid URL: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = "Required request parameter '" + ex.getParameterName() + "' is missing";
        log.error(message, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(message));
    }
}
