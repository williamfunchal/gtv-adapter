package com.consensus.gtvadapter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws.ddb")
public class DynamoDBProperties {

    private String dataMappingTable;
}
