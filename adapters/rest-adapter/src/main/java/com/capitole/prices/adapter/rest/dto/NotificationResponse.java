package com.capitole.prices.adapter.rest.dto;

import java.time.LocalDateTime;

public record NotificationResponse(String description, LocalDateTime responseTime, String code) {
}
