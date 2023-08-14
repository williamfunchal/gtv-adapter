package com.consensus.gtvadapter;

import com.consensus.gtvadapter.config.DynamoDBProperties;
import com.consensus.gtvadapter.config.GtvProperties;
import com.consensus.gtvadapter.config.QueueProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.consensus.gtvadapter",
        "com.consensus.common"
})
@EnableConfigurationProperties({QueueProperties.class, GtvProperties.class, DynamoDBProperties.class})
public class GtvAdapter {
    
    public static void main(String[] args) {
        SpringApplication.run(GtvAdapter.class, args);
    }
}

