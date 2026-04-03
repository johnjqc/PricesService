package com.capitole.prices.application.dto;


public sealed interface PriceResult<T, E>
        permits PriceSuccess, PriceFailure {

    static <T, E> PriceSuccess<T, E> ok(T value) {
        return new PriceSuccess<>(value);
    }

    static <T, E> PriceFailure<T, E> fail(E error) {
        return new PriceFailure<>(error);
    }
}



