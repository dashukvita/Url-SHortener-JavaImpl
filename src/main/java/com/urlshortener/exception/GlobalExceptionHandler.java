package com.urlshortener.exception;

import com.urlshortener.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UrlValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleUrlAlreadyExistsException(UrlValidationException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidUrl(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(ex.getMessage()));
    }
}
