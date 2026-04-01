package com.capitole.prices.adapter.rest.advice;

import com.capitole.prices.adapter.rest.dto.ApiResponse;
import com.capitole.prices.adapter.rest.dto.NotificationResponse;
import com.capitole.prices.adapter.rest.dto.PriceResponse;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<PriceResponse>> ExceptionHandler(Exception e) {

        log.error("Internal error", e);

        NotificationResponse notificationResponse = new NotificationResponse(
                "Internal Error, see logs",
                LocalDateTime.now(),
                "ERR-00");
        ApiResponse<PriceResponse> apiResponse = new ApiResponse<>(null, notificationResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<PriceResponse>> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {

        log.error("Request param error from user", e);

        NotificationResponse notificationResponse = new NotificationResponse(
                "Bad request Error, see logs",
                LocalDateTime.now(),
                "ERR-01");
        ApiResponse<PriceResponse> apiResponse = new ApiResponse<>(null, notificationResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<PriceResponse>> NoResourceFoundExceptionHandler(NoResourceFoundException e) {

        log.error("Internal error", e);

        NotificationResponse notificationResponse = new NotificationResponse(
                "Request resource not foud Error, see logs",
                LocalDateTime.now(),
                "ERR-02");
        ApiResponse<PriceResponse> apiResponse = new ApiResponse<>(null, notificationResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
