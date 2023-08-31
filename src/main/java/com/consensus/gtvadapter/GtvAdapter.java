package com.consensus.gtvadapter;

import com.consensus.gtvadapter.config.properties.DynamoDBProperties;
import com.consensus.gtvadapter.config.properties.GtvProperties;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.consensus.gtvadapter",
        "com.consensus.common.sqs",
        "com.consensus.common.logging",
        "com.consensus.common.metrics",
        "com.consensus.common.dynamo.db",
        "com.consensus.common.health"
})
@EnableConfigurationProperties({QueueProperties.class, GtvProperties.class, DynamoDBProperties.class})
public class GtvAdapter {
    
    public static void main(String[] args) {
        SpringApplication.run(GtvAdapter.class, args);
    }
}

