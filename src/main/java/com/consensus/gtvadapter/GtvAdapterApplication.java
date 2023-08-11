package com.consensus.gtvadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.consensus.gtvadapter.config.GtvProperties;
import com.consensus.gtvadapter.config.QueueProperties;


@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.consensus.gtvadapter",
        "com.consensus.common"
})
@EnableConfigurationProperties({QueueProperties.class, GtvProperties.class})
public class GtvAdapterApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GtvAdapterApplication.class, args);
    }
}

