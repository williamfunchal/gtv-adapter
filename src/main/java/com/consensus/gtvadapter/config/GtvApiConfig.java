package com.consensus.gtvadapter.config;

import com.consensus.common.web.DownstreamWebClient;
import com.consensus.gtvadapter.config.properties.GtvProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GtvApiConfig {

    @Bean
    @Qualifier("gtvWebClient")
    public DownstreamWebClient gtvWebClient(WebClient reactiveWebClient, GtvProperties gtvProperties) {
        return DownstreamWebClient.builder()
                .resourceName("GTV API")
                .webClient(reactiveWebClient)
                .baseUrl(gtvProperties.getHost())
                .build();
    }
}
