package com.capitole.prices.adapter.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(
        Long productId,
        Long brandId,
        Long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price
) {}