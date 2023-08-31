package com.consensus.gtvadapter.config;

import com.consensus.gtvadapter.config.properties.SecurityPathsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.stream.Collectors;

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
@EnableConfigurationProperties(SecurityPathsProperties.class)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
            ServerWebExchangeMatcher permitAllMatcher) {
        http
                .csrf().disable()
                .cors()
                .and()

                // stateless sessions
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                .authorizeExchange()
                .matchers(permitAllMatcher)
                .permitAll()
                .and()

                .authorizeExchange()
                .anyExchange()
                .authenticated();

        return http.build();

    }

    @Bean
    public ServerWebExchangeMatcher permitAllMatcher(SecurityPathsProperties pathsProperties) {
        return new OrServerWebExchangeMatcher(pathsProperties.getPermit()
                .stream()
                .map(PathPatternParserServerWebExchangeMatcher::new)
                .collect(Collectors.toList()));
    }
}
