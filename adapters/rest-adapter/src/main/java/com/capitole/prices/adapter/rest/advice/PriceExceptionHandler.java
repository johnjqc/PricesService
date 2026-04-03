package com.capitole.prices.adapter.rest.advice;

import com.capitole.prices.adapter.rest.dto.ApiResponseDto;
import com.capitole.prices.adapter.rest.factory.ApiResponseFactory;
import com.capitole.prices.adapter.rest.dto.PriceResponse;
import com.capitole.prices.domain.exception.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class PriceExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(PriceExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> handleConstraintViolationException(ConstraintViolationException e) {

        log.error("Validation error", e);

        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse(ErrorCode.VALIDATION_ERROR.getDescription());

        return ResponseEntity.badRequest()
                .body(ApiResponseFactory.error(ErrorCode.VALIDATION_ERROR, message, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {

        log.error("Requested param error from user", e);

        return ResponseEntity.badRequest()
                .body(ApiResponseFactory.error(ErrorCode.VALIDATION_ERROR, e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> NoResourceFoundExceptionHandler(NoResourceFoundException e) {

        log.error("Internal error", e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseFactory.error(ErrorCode.RESOURCE_NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<PriceResponse>> handleGenericException(Exception e) {

        log.error("Internal error", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseFactory.error(ErrorCode.INTERNAL_ERROR, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
