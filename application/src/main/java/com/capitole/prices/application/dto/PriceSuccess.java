package com.capitole.prices.application.dto;

public record PriceSuccess<T, E>(T value)
        implements PriceResult<T, E> {
}
