package com.capitole.prices.application.usecase;

import com.capitole.prices.application.port.in.GetProductPrice;
import com.capitole.prices.domain.exception.PriceNotFoundException;
import com.capitole.prices.domain.model.Price;
import com.capitole.prices.application.port.outp.PriceRepositoryPort;

import java.time.LocalDateTime;
import java.util.Optional;

public class GetProductPriceUseCase implements GetProductPrice {

    private final PriceRepositoryPort priceRepository;

    public GetProductPriceUseCase(PriceRepositoryPort priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price getPrice(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate
    ) {

        Optional<Price> prices = priceRepository.findApplicablePrice(
                brandId,
                productId,
                applicationDate
        );

        return prices.orElseThrow(() -> new PriceNotFoundException(productId, brandId));
    }
}