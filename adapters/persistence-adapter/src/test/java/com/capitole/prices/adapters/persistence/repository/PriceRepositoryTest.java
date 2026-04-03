package com.capitole.prices.adapters.persistence.repository;

import com.capitole.prices.adapters.persistence.config.JpaTestConfig;
import com.capitole.prices.adapters.persistence.entity.PriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = JpaTestConfig.class)
class PriceRepositoryTest {

    @Autowired
    private PriceJpaRepository priceRepository;

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

    @Test
    @DisplayName("Should return applicable prices for 2020-06-14 10:00")
    void givenProductIdAndBrandId_whenQueryPriceAt10AMOnJune14_thenReturnApplicablePrices() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);

        List<PriceEntity> result = priceRepository.findByBrandProductAndDate(
                PRODUCT_ID,
                BRAND_ID,
                date
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriceList()).isEqualTo(1);
        assertThat(result.get(0).getPrice()).isEqualByComparingTo("35.50");
    }

    @Test
    @DisplayName("Should return multiple applicable prices for 2020-06-14 16:00")
    void givenProductIdAndBrandId_whenQueryPriceAt4PMOnJune14_thenReturnMultiplePrices() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        List<PriceEntity> result = priceRepository.findByBrandProductAndDate(
                PRODUCT_ID,
                BRAND_ID,
                date
        );

        // At 16:00, both PriceList 1 and PriceList 2 apply
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return correct price for 2020-06-14 21:00")
    void givenProductIdAndBrandId_whenQueryPriceAt9PMOnJune14_thenReturnPriceList1() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 21, 0);

        List<PriceEntity> result = priceRepository.findByBrandProductAndDate(
                PRODUCT_ID,
                BRAND_ID,
                date
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriceList()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return price when exactly at start boundary time")
    void givenPriceAtExactStartTime_whenQueryPrice_thenReturnPrice() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 15, 0);

        List<PriceEntity> result = priceRepository.findByBrandProductAndDate(
                PRODUCT_ID,
                BRAND_ID,
                date
        );

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should return empty when no price applies for given date range")
    void givenDateWithNoApplicablePrice_whenQueryPrice_thenReturnEmpty() {

        LocalDateTime date = LocalDateTime.of(2020, 6, 13, 10, 0);

        List<PriceEntity> result = priceRepository.findByBrandProductAndDate(
                PRODUCT_ID,
                BRAND_ID,
                date
        );

        assertThat(result).isEmpty();
    }

}