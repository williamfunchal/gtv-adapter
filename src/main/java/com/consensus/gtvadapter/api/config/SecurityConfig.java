package com.consensus.gtvadapter.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http
                .csrf().disable()
                .cors()
                .and();

        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http.authorizeRequests()
            .antMatchers("/health", "/status/**", "/status", "/docs/**","/billing/**")
            .permitAll()
            .and()
            //.addFilterBefore(tokenAuthorizationFilter, BasicAuthenticationFilter.class)
            .authorizeRequests()
            .anyRequest()
            .authenticated().and().httpBasic();
        return http.build();

    }
}
