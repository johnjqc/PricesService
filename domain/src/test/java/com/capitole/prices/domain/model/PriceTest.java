package com.capitole.prices.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    private static final Long BRAND_ID = 1L;
    private static final Long PRODUCT_ID = 35455L;

    @Test
    void givenSingleApplicablePrice_whenResolveHighestPriority_thenReturnThatPrice() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        Price price = new Price(
                BRAND_ID,
                PRODUCT_ID,
                1,
                0,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                BigDecimal.valueOf(35.50),
                "EUR"
        );

        Optional<Price> result = Price.resolveHighestPriority(List.of(price), applicationDate);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(price);
    }

    @Test
    void givenMultipleApplicablePrices_whenResolveHighestPriority_thenReturnHighestPriority() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price lowPriority = new Price(
                BRAND_ID,
                PRODUCT_ID,
                1,
                0,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                BigDecimal.valueOf(35.50),
                "EUR"
        );

        Price highPriority = new Price(
                BRAND_ID,
                PRODUCT_ID,
                2,
                1,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                BigDecimal.valueOf(25.45),
                "EUR"
        );

        Optional<Price> result = Price.resolveHighestPriority(
                List.of(lowPriority, highPriority),
                applicationDate
        );

        assertThat(result).isPresent();
        assertThat(result.get().priority()).isEqualTo(1);
    }

    @Test
    void givenPriceOutsideDateRange_whenResolveHighestPriority_thenReturnEmpty() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);

        Price price = new Price(
                BRAND_ID,
                PRODUCT_ID,
                1,
                0,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 6, 14, 23, 59),
                BigDecimal.valueOf(35.50),
                "EUR"
        );

        Optional<Price> result = Price.resolveHighestPriority(List.of(price), applicationDate);

        assertThat(result).isEmpty();
    }

    @Test
    void givenMultiplePricesButNoneApplicable_whenResolveHighestPriority_thenReturnEmpty() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 13, 10, 0);

        Price price1 = new Price(
                BRAND_ID,
                PRODUCT_ID,
                1,
                0,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 6, 14, 23, 59),
                BigDecimal.valueOf(35.50),
                "EUR"
        );

        Price price2 = new Price(
                BRAND_ID,
                PRODUCT_ID,
                2,
                1,
                LocalDateTime.of(2020, 6, 15, 0, 0),
                LocalDateTime.of(2020, 6, 15, 23, 59),
                BigDecimal.valueOf(30.50),
                "EUR"
        );

        Optional<Price> result = Price.resolveHighestPriority(
                List.of(price1, price2),
                applicationDate
        );

        assertThat(result).isEmpty();
    }

    @Test
    void givenApplicationDateEqualsStartDate_whenResolveHighestPriority_thenPriceIsApplicable() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 0, 0);

        Price price = new Price(
                BRAND_ID,
                PRODUCT_ID,
                1,
                0,
                applicationDate,
                LocalDateTime.of(2020, 6, 14, 23, 59),
                BigDecimal.valueOf(35.50),
                "EUR"
        );

        Optional<Price> result = Price.resolveHighestPriority(List.of(price), applicationDate);

        assertThat(result).isPresent();
    }

    @Test
    void givenApplicationDateEqualsEndDate_whenResolveHighestPriority_thenPriceIsApplicable() {

        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59);

        Price price = new Price(
                BRAND_ID,
                PRODUCT_ID,
                1,
                0,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                endDate,
                BigDecimal.valueOf(35.50),
                "EUR"
        );

        Optional<Price> result = Price.resolveHighestPriority(List.of(price), endDate);

        assertThat(result).isPresent();
    }
}
