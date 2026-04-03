package com.capitole.prices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record Price(

        Long brandId,
        Long productId,
        Integer priceList,
        Integer priority,

        LocalDateTime startDate,
        LocalDateTime endDate,

        BigDecimal price,
        String currency

) {

    public static Optional<Price> resolveHighestPriority(List<Price> prices, LocalDateTime applicationDate) {
        return prices.stream()
                .filter(price -> isApplicable(price, applicationDate))
                .max(Comparator.comparing(Price::priority));
    }

    private static boolean isApplicable(Price price, LocalDateTime applicationDate) {
        return !price.startDate().isAfter(applicationDate)
                && !price.endDate().isBefore(applicationDate);
    }
}