package com.capitole.prices.adapter.rest.advice;

import com.capitole.prices.adapter.rest.dto.ApiResponseDto;
import com.capitole.prices.adapter.rest.dto.NotificationResponse;
import com.capitole.prices.adapter.rest.dto.PriceResponse;
import com.capitole.prices.domain.exception.DomainException;
import com.capitole.prices.domain.exception.ErrorCode;
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
                .orElse(ErrorCode.VALIDATION_ERROR.getDescription());

        return buildResponse(ErrorCode.VALIDATION_ERROR, message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {

        log.error("Requested param error from user", e);

        return buildResponse(ErrorCode.VALIDATION_ERROR, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> NoResourceFoundExceptionHandler(NoResourceFoundException e) {

        log.error("Internal error", e);

        return buildResponse(ErrorCode.RESOURCE_NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> handleDomainException(DomainException e) {

        log.error("Domain exception", e);

        HttpStatus status = switch (e.getErrorCode()) {
            case PRODUCT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return buildResponse(e.getErrorCode(), e.getMessage(), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> handleGenericException(Exception e) {

        log.error("Internal error", e);

        return buildResponse(ErrorCode.INTERNAL_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponseDto<PriceResponse>> buildResponse(
            ErrorCode errorCode,
            String message,
            HttpStatus status) {

        NotificationResponse notificationResponse = new NotificationResponse(
                message,
                LocalDateTime.now(),
                errorCode.getCode());

        ApiResponseDto<PriceResponse> apiResponse = new ApiResponseDto<>(null, notificationResponse);
        return ResponseEntity.status(status).body(apiResponse);
    }
}
