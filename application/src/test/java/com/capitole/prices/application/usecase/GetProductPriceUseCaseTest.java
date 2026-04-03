package com.capitole.prices.application.usecase;

import com.capitole.prices.application.dto.ApplicationError;
import com.capitole.prices.application.dto.PriceFailure;
import com.capitole.prices.application.dto.PriceResult;
import com.capitole.prices.application.dto.PriceSuccess;
import com.capitole.prices.application.port.in.GetProductPrice;
import com.capitole.prices.domain.exception.ErrorCode;
import com.capitole.prices.domain.model.Price;
import com.capitole.prices.application.port.outp.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GetProductPriceUseCaseTest {

    private PriceRepositoryPort priceRepository;
    private GetProductPrice useCase;

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;
    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 16, 0);

    @BeforeEach
    void setUp() {
        priceRepository = Mockito.mock(PriceRepositoryPort.class);
        useCase = new GetProductPriceUseCase(priceRepository);
    }

    @Test
    @DisplayName("Should return price when applicable price exists")
    void givenExistingPrice_whenExecute_thenReturnPrice() {

        Price expectedPrice = new Price(
                BRAND_ID,
                PRODUCT_ID,
                2,
                1,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                new BigDecimal("25.45"),
                "EUR"
        );

        when(priceRepository.findApplicablePrices(eq(BRAND_ID), eq(PRODUCT_ID), eq(APPLICATION_DATE)))
                .thenReturn(List.of(expectedPrice));

        PriceResult<Price, ApplicationError> result = useCase.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result).isInstanceOf(PriceSuccess.class);
        Price price = ((PriceSuccess<Price, ApplicationError>) result).value();

        assertThat(result).isNotNull();
        assertThat(price.productId()).isEqualTo(PRODUCT_ID);
        assertThat(price.brandId()).isEqualTo(BRAND_ID);
        assertThat(price.priceList()).isEqualTo(2);
        assertThat(price.price()).isEqualByComparingTo(new BigDecimal("25.45"));
    }

    @Test
    void givenNoPrice_whenExecute_thenReturnFailure() {

        when(priceRepository.findApplicablePrices(
                eq(BRAND_ID),
                eq(PRODUCT_ID),
                eq(APPLICATION_DATE)))
                .thenReturn(List.of());

        PriceResult<Price, ApplicationError> result =
                useCase.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result).isInstanceOf(PriceFailure.class);

        if (result instanceof PriceFailure<Price, ApplicationError>(ApplicationError error)) {
            assertThat(error.code()).isEqualTo(ErrorCode.PRICE_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("Should return highest priority price when multiple prices apply")
    void givenMultiplePricesWithDifferentPriorities_whenExecute_thenReturnHighestPriority() {

        Price lowerPriorityPrice = new Price(
                BRAND_ID,
                PRODUCT_ID,
                1,
                0,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 6, 14, 23, 59),
                new BigDecimal("35.50"),
                "EUR"
        );

        Price higherPriorityPrice = new Price(
                BRAND_ID,
                PRODUCT_ID,
                2,
                1,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                new BigDecimal("25.45"),
                "EUR"
        );

        when(priceRepository.findApplicablePrices(eq(BRAND_ID), eq(PRODUCT_ID), eq(APPLICATION_DATE)))
                .thenReturn(List.of(lowerPriorityPrice, higherPriorityPrice));

        PriceResult<Price, ApplicationError> result = useCase.findApplicablePrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result).isInstanceOf(PriceSuccess.class);
        Price price = ((PriceSuccess<Price, ApplicationError>) result).value();

        assertThat(price.priority()).isEqualTo(1);
        assertThat(price.priceList()).isEqualTo(2);
    }
}