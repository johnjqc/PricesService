package com.capitole.prices.adapters.persistence.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.capitole.prices.adapters.persistence")
@EntityScan("com.capitole.prices.adapters.persistence.entity")
public class JpaTestConfig {
}
