package com.capitole.prices.adapters.persistence.adapter;

import com.capitole.prices.adapters.persistence.entity.PriceEntity;
import com.capitole.prices.adapters.persistence.mapper.PriceMapper;
import com.capitole.prices.adapters.persistence.repository.PriceJpaRepository;
import com.capitole.prices.domain.model.Price;
import com.capitole.prices.application.port.outp.PriceRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository repository;
    private final PriceMapper priceMapper;

    public PriceRepositoryAdapter(PriceJpaRepository repository, PriceMapper priceMapper) {
        this.repository = repository;
        this.priceMapper = priceMapper;
    }

    @Override
    public List<Price> findApplicablePrices(Long brandId, Long productId, LocalDateTime applicationDate) {

        List<PriceEntity> entities = repository.findByBrandProductAndDate(
                productId,
                brandId,
                applicationDate
        );

        return entities.stream()
                .map(priceMapper::toDomain)
                .toList();
    }
}