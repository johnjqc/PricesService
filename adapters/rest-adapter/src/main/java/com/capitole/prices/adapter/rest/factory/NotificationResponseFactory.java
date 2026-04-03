package com.capitole.prices.adapter.rest.factory;

import com.capitole.prices.adapter.rest.dto.NotificationResponse;
import com.capitole.prices.domain.exception.ErrorCode;

import java.time.LocalDateTime;

public class NotificationResponseFactory {

    private NotificationResponseFactory() {
    }

    public static NotificationResponse success() {
        return new NotificationResponse(
                ErrorCode.SUCCESS.getDescription(),
                LocalDateTime.now(),
                ErrorCode.SUCCESS.getCode()
        );
    }

    public static NotificationResponse error(ErrorCode errorCode, String message) {
        return new NotificationResponse(
                message,
                LocalDateTime.now(),
                errorCode.getCode()
        );
    }
}