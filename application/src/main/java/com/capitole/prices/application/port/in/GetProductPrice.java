package com.capitole.prices.application.port.in;

import com.capitole.prices.application.dto.ApplicationError;
import com.capitole.prices.application.dto.PriceResult;
import com.capitole.prices.domain.model.Price;

import java.time.LocalDateTime;

public interface GetProductPrice {

    PriceResult<Price, ApplicationError> findApplicablePrice(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate
    );
}
