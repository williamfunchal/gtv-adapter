package com.consensus.gtvadapter.config;

import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.consensus.gtvadapter.processor.persistence.repository")
public class RdsDatabaseConfig {

    private static final String DB_QUERY = "SELECT CURRENT_DATE FROM dual";

    @Bean
    @Profile("local")
    public DataSourceHealthIndicator coreDbHealthIndicator(DataSource dataSource) {
        return new DataSourceHealthIndicator(dataSource, DB_QUERY);
    }
}
