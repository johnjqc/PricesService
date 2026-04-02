package com.capitole.prices.application.port.in;

import com.capitole.prices.domain.model.Price;

import java.time.LocalDateTime;

public interface GetProductPrice {

    Price getPrice(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate
    );
}
