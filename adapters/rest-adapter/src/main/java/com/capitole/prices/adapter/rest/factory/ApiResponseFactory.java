package com.capitole.prices.adapter.rest.factory;

import com.capitole.prices.adapter.rest.dto.ApiResponseDto;
import com.capitole.prices.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ApiResponseFactory {

    private ApiResponseFactory() {
    }

    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(data, NotificationResponseFactory.success());
    }

    public static <T> ApiResponseDto<T> error(ErrorCode errorCode, String message, HttpStatus status) {
        return new ApiResponseDto<>(null, NotificationResponseFactory.error(errorCode, message));
    }
}