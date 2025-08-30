package com.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private String error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> error(String error) {
        return new ResponseDto<>(false, null, error);
    }
}
