package com.capitole.prices.adapters.persistence.adapter;

import com.capitole.prices.adapters.persistence.entity.PriceEntity;
import com.capitole.prices.adapters.persistence.mapper.PriceMapper;
import com.capitole.prices.adapters.persistence.repository.PriceJpaRepository;
import com.capitole.prices.domain.model.Price;
import com.capitole.prices.domain.port.PriceRepositoryPort;

import java.time.LocalDateTime;
import java.util.Optional;

public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository repository;
    private final PriceMapper priceMapper;

    public PriceRepositoryAdapter(PriceJpaRepository repository, PriceMapper priceMapper) {
        this.repository = repository;
        this.priceMapper = priceMapper;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {

        Optional<PriceEntity> entity = repository.findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                productId,
                brandId,
                applicationDate,
                applicationDate
        );

        return entity.map(priceMapper::toDomain);
    }
}