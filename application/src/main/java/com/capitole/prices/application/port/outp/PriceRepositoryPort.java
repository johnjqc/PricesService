package com.capitole.prices.application.port.outp;

import com.capitole.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {

    List<Price> findApplicablePrices(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate
    );
}