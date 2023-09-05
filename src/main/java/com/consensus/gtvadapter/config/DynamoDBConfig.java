package com.consensus.gtvadapter.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.consensus.common.dynamo.db.CCSIDynamoHealthCheckContributor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDBMapper getDynamoDBMapper(AmazonDynamoDB amazonDynamoDB){
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Bean
    public DynamoDB notifyDynamoDbClient(AmazonDynamoDB dynamoDbService) {
        return new DynamoDB(dynamoDbService);
    }

    @Bean
    public DynamoDBHealthCheckContributor dynamoDBHealthCheckContributor(@Value("${ENVIRONMENT}") String env,
            @Value("${NAMESPACE}") String namespace) {
        return new DynamoDBHealthCheckContributor(env, namespace);
    }

    @Getter
    public static class DynamoDBHealthCheckContributor implements CCSIDynamoHealthCheckContributor {

        private static final List<String> BILLING_TABLES = List.of(
                "usage_events",
                "customer_events",
                "service_events",
                "service_resource_events",
                "account_mapping",
                "service_resource_mapping",
                "gtv_api_calls"
        );

        private final List<String> tableNames;

        public DynamoDBHealthCheckContributor(String env, String namespace) {
            this.tableNames = BILLING_TABLES.stream()
                    .map(tableName -> env + "_" + namespace + "_" + tableName)
                    .collect(toList());
        }
    }
}
