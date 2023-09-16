package com.consensus.gtvadapter.config;

import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RdsDatabaseConfig {

    private static final String DB_QUERY = "SELECT CURRENT_DATE FROM dual";

    @Bean
    public DataSourceHealthIndicator coreDbHealthIndicator(DataSource dataSource) {
        return new DataSourceHealthIndicator(dataSource, DB_QUERY);
    }
}
