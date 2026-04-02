package com.capitole.prices.adapters.persistence.repository;

import com.capitole.prices.adapters.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

    Optional<PriceEntity> findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
            Long productId,
            Long brandId,
            LocalDateTime applicationDate1,
            LocalDateTime applicationDate2
    );

}