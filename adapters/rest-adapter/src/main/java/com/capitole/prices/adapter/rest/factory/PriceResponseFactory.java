package com.capitole.prices.adapter.rest.factory;

import com.capitole.prices.adapter.rest.dto.PriceResponse;
import com.capitole.prices.domain.model.Price;

public class PriceResponseFactory {

    private PriceResponseFactory() {
    }

    public static PriceResponse fromDomain(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price(),
                price.currency()
        );
    }
}