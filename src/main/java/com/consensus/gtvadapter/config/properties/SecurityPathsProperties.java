package com.consensus.gtvadapter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("security.paths")
public class SecurityPathsProperties {

    private List<String> permit;
}
