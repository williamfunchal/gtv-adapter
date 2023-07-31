package com.consensus.gtvadapter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("gtv")
public class GtvProperties {
    private String host;
    private String apiXKey;
}
