package com.capitole.prices.application.dto;

import com.capitole.prices.domain.exception.ErrorCode;

public record ApplicationError(
        ErrorCode code,
        String message
) {
}
