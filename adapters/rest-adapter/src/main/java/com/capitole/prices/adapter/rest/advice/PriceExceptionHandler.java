package com.capitole.prices.adapter.rest.advice;

import com.capitole.prices.adapter.rest.dto.ApiResponseDto;
import com.capitole.prices.adapter.rest.dto.NotificationResponse;
import com.capitole.prices.adapter.rest.dto.PriceResponse;
import com.capitole.prices.application.exception.ProductNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class PriceExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(PriceExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> handleConstraintViolationException(ConstraintViolationException e) {

        log.error("Validation error", e);

        String message = e.getConstraintViolations().stream()
                .map(v -> v.getMessage())
                .findFirst()
                .orElse("Validation error");

        NotificationResponse notificationResponse = new NotificationResponse(
                message,
                LocalDateTime.now(),
                "ERR-01");
        ApiResponseDto<PriceResponse> apiResponse = new ApiResponseDto<>(null, notificationResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {

        log.error("Request param error from user", e);

        NotificationResponse notificationResponse = new NotificationResponse(
                "Bad request Error, see logs",
                LocalDateTime.now(),
                "ERR-01");
        ApiResponseDto<PriceResponse> apiResponse = new ApiResponseDto<>(null, notificationResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> NoResourceFoundExceptionHandler(NoResourceFoundException e) {

        log.error("Internal error", e);

        NotificationResponse notificationResponse = new NotificationResponse(
                "Request resource not foud Error, see logs",
                LocalDateTime.now(),
                "ERR-02");
        ApiResponseDto<PriceResponse> apiResponse = new ApiResponseDto<>(null, notificationResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> NoResourceFoundExceptionHandler(ProductNotFoundException e) {

        log.error("Requested product not found", e);

        NotificationResponse notificationResponse = new NotificationResponse(
                "Request product not foud",
                LocalDateTime.now(),
                "ERR-03");
        ApiResponseDto<PriceResponse> apiResponse = new ApiResponseDto<>(null, notificationResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
