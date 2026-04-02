package com.capitole.prices.bootstrap.config;

import com.capitole.prices.adapters.persistence.adapter.PriceRepositoryAdapter;
import com.capitole.prices.adapters.persistence.mapper.PriceMapper;
import com.capitole.prices.adapters.persistence.repository.PriceJpaRepository;
import com.capitole.prices.application.port.in.GetProductPrice;
import com.capitole.prices.application.usecase.GetProductPriceUseCase;
import com.capitole.prices.application.port.outp.PriceRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceConfig {

    @Bean
    public PriceRepositoryPort priceRepository(
            PriceJpaRepository jpaRepository,
            PriceMapper priceMapper) {

        return new PriceRepositoryAdapter(jpaRepository, priceMapper);
    }

    @Bean
    public GetProductPrice getProductPriceUseCase(
            PriceRepositoryPort repository) {
        return new GetProductPriceUseCase(repository);
    }
}
