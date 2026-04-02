package com.capitole.prices.adapters.persistence.mapper;

import com.capitole.prices.adapters.persistence.entity.PriceEntity;
import com.capitole.prices.domain.model.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

    public Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getProductId(),
                entity.getPriceList(),
                entity.getPriority(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }
}
