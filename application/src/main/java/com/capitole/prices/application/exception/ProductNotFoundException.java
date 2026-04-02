package com.capitole.prices.application.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long productId, Long brandId) {

        super("Could not find product: " + productId + " - brandId: " + brandId);
    }
}
