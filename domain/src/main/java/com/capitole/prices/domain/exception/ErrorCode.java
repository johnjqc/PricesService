package com.capitole.prices.domain.exception;

public enum ErrorCode {

    SUCCESS("SCS-00", "Operation successful"),
    VALIDATION_ERROR("ERR-01", "Validation error"),
    RESOURCE_NOT_FOUND("ERR-02", "Resource not found"),
    PRICE_NOT_FOUND("ERR-03", "Price not found"),
    INTERNAL_ERROR("ERR-00", "Internal server error");

    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}