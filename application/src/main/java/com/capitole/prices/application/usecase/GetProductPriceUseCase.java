package com.capitole.prices.application.usecase;

import com.capitole.prices.application.dto.ApplicationError;
import com.capitole.prices.application.dto.PriceResult;
import com.capitole.prices.application.port.in.GetProductPrice;
import com.capitole.prices.domain.exception.ErrorCode;
import com.capitole.prices.domain.model.Price;
import com.capitole.prices.application.port.outp.PriceRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class GetProductPriceUseCase implements GetProductPrice {

    private final PriceRepositoryPort priceRepository;

    public GetProductPriceUseCase(
            PriceRepositoryPort priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceResult<Price, ApplicationError> findApplicablePrice(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate
    ) {
        List<Price> prices = priceRepository.findApplicablePrices(
                brandId,
                productId,
                applicationDate
        );

        return Price.resolveHighestPriority(prices, applicationDate)
                .<PriceResult<Price, ApplicationError>>map(PriceResult::ok)
                .orElseGet(() -> PriceResult.fail(
                        new ApplicationError(
                                ErrorCode.PRICE_NOT_FOUND,
                                String.format("Could not find product: %d - brandId: %d", productId, brandId)
                        )
                ));
    }
}