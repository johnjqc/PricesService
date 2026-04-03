package com.capitole.prices.application.dto;

public record PriceFailure<T, E>(E error)
        implements PriceResult<T, E> {
}
