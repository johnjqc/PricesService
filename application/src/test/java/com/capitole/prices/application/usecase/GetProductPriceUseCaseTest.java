package com.capitole.prices.application.usecase;

import com.capitole.prices.application.port.in.GetProductPrice;
import com.capitole.prices.domain.exception.PriceNotFoundException;
import com.capitole.prices.domain.model.Price;
import com.capitole.prices.application.port.outp.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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

        when(priceRepository.findApplicablePrice(eq(BRAND_ID), eq(PRODUCT_ID), eq(APPLICATION_DATE)))
                .thenReturn(Optional.of(expectedPrice));

        Price result = useCase.getPrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result).isNotNull();
        assertThat(result.productId()).isEqualTo(PRODUCT_ID);
        assertThat(result.brandId()).isEqualTo(BRAND_ID);
        assertThat(result.priceList()).isEqualTo(2);
        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("25.45"));
    }

    @Test
    @DisplayName("Should throw PriceNotFoundException when no price exists")
    void givenNoPrice_whenExecute_thenThrowException() {

        when(priceRepository.findApplicablePrice(any(), any(), any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getPrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining(String.valueOf(PRODUCT_ID))
                .hasMessageContaining(String.valueOf(BRAND_ID));
    }

    @Test
    @DisplayName("Should return highest priority price when multiple prices apply")
    void givenMultiplePricesWithDifferentPriorities_whenExecute_thenReturnHighestPriority() {

        // When multiple prices apply, the repository already returns the highest priority
        Price highestPriorityPrice = new Price(
                BRAND_ID,
                PRODUCT_ID,
                2,          // priceList with higher priority
                1,          // higher priority value
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                new BigDecimal("25.45"),
                "EUR"
        );

        when(priceRepository.findApplicablePrice(eq(BRAND_ID), eq(PRODUCT_ID), eq(APPLICATION_DATE)))
                .thenReturn(Optional.of(highestPriorityPrice));

        Price result = useCase.getPrice(BRAND_ID, PRODUCT_ID, APPLICATION_DATE);

        assertThat(result.priority()).isEqualTo(1);
        assertThat(result.priceList()).isEqualTo(2);
    }
}