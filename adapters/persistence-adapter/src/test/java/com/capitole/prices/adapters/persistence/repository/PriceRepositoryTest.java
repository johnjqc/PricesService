package com.capitole.prices.adapters.persistence.repository;

import com.capitole.prices.adapters.persistence.config.JpaTestConfig;
import com.capitole.prices.adapters.persistence.entity.PriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = JpaTestConfig.class)
class PriceRepositoryTest {

    @Autowired
    private PriceJpaRepository priceRepository;

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

    @Test
    @DisplayName("Should return correct price for 2020-06-14 10:00")
    void givenProductIdAndBrandId_whenQueryPriceAt10AMOnJune14_thenReturnPriceList1WithPrice() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);

        Optional<PriceEntity> result =
                priceRepository
                        .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                PRODUCT_ID,
                                BRAND_ID,
                                date,
                                date
                        );

        assertThat(result).isPresent();
        assertThat(result.get().getPriceList()).isEqualTo(1);
        assertThat(result.get().getPrice()).isEqualByComparingTo("35.50");
    }

    @Test
    @DisplayName("Should return correct price for 2020-06-14 16:00")
    void givenProductIdAndBrandId_whenQueryPriceAt4PMOnJune14_thenReturnPriceList1WithPrice() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        Optional<PriceEntity> result =
                priceRepository
                        .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                PRODUCT_ID,
                                BRAND_ID,
                                date,
                                date
                        );

        assertThat(result).isPresent();
        assertThat(result.get().getPriceList()).isEqualTo(2);
        assertThat(result.get().getPrice()).isEqualByComparingTo("25.45");
    }

    @Test
    @DisplayName("Should return correct price for 2020-06-14 21:00")
    void givenProductIdAndBrandId_whenQueryPriceAt9PMOnJune14_thenReturnPriceList1WithPrice() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 21, 0);

        Optional<PriceEntity> result =
                priceRepository
                        .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                PRODUCT_ID,
                                BRAND_ID,
                                date,
                                date
                        );

        assertThat(result).isPresent();
        assertThat(result.get().getPriceList()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return highest priority when multiple prices apply at same time")
    void givenMultiplePricesAtSameTime_whenQueryPrice_thenReturnHighestPriority() {

        // At 2020-06-14 16:00, TWO prices apply:
        // - PriceList 1: priority 0 (valid 00:00-23:59)
        // - PriceList 2: priority 1 (valid 15:00-18:30)
        // Expected: PriceList 2 (higher priority)
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        Optional<PriceEntity> result = priceRepository
                .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        PRODUCT_ID,
                        BRAND_ID,
                        date,
                        date
                );

        assertThat(result).isPresent();
        assertThat(result.get().getPriority()).isEqualTo(1);
        assertThat(result.get().getPriceList()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return price when exactly at start boundary time")
    void givenPriceAtExactStartTime_whenQueryPrice_thenReturnPrice() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 15, 0);

        Optional<PriceEntity> result = priceRepository
                .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        PRODUCT_ID,
                        BRAND_ID,
                        date,
                        date
                );

        assertThat(result).isPresent();
        assertThat(result.get().getPriceList()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return empty when no price applies for given date range")
    void givenDateWithNoApplicablePrice_whenQueryPrice_thenReturnEmpty() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 13, 10, 0);

        Optional<PriceEntity> result = priceRepository
                .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        PRODUCT_ID,
                        BRAND_ID,
                        date,
                        date
                );

        assertThat(result).isEmpty();
    }

}