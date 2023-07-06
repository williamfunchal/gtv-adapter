package com.consensus.gtvadapter.api;

import com.consensus.gtvadapter.api.config.GtvProperties;
import com.consensus.gtvadapter.api.config.QueueProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.consensus.gtvadapter",
        "com.consensus.common"
})
@EnableConfigurationProperties({QueueProperties.class, GtvProperties.class})
public class AdapterApi {
    
    public static void main(String[] args) {
        SpringApplication.run(AdapterApi.class, args);
    }
}

