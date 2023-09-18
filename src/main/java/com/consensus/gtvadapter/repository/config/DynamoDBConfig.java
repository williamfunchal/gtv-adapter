package com.consensus.gtvadapter.repository.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverterFactory;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.consensus.common.dynamo.db.CCSIDynamoHealthCheckContributor;
import lombok.Getter;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix;
import static java.util.stream.Collectors.toList;

@Configuration
@EnableDynamoDBRepositories(dynamoDBMapperConfigRef = "dynamoDBMapperConfig", basePackages = "com.consensus.gtvadapter.repository.storage")
public class DynamoDBConfig {

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig(@Value("${ENVIRONMENT}") String env,
            @Value("${NAMESPACE}") String namespace) {
        return new DynamoDBMapperConfig.Builder()
                .withTypeConverterFactory(DynamoDBTypeConverterFactory.standard())
                .withTableNameOverride(withTableNamePrefix(String.format("%s_%s_", env, namespace)))
                .build();
    }

    @Bean
    public DynamoDB dynamoDbClient(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDB(amazonDynamoDB);
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
                    .map(tableName -> String.format("%s_%s_%s", env, namespace, tableName))
                    .collect(toList());
        }
    }
}
