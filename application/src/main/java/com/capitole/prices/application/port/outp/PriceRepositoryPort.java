package com.capitole.prices.application.port.outp;

import com.capitole.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepositoryPort {

    Optional<Price> findApplicablePrice(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate
    );
}