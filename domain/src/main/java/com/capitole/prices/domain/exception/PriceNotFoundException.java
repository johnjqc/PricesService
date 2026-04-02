package com.capitole.prices.domain.exception;

public class PriceNotFoundException extends DomainException {

    public PriceNotFoundException(Long productId, Long brandId) {
        super(
                ErrorCode.PRODUCT_NOT_FOUND,
                String.format("Could not find product: %d - brandId: %d", productId, brandId)
        );
    }
}