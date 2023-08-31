package com.consensus.gtvadapter.config;

import com.consensus.common.dynamo.db.CCSIDynamoHealthCheckContributor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Component
public class DynamoDBHealthCheckContributor implements CCSIDynamoHealthCheckContributor {

    private static final List<String> BILLING_TABLES = List.of(
            "bill-ptf_account_mapping",
            "bill-ptf_data_mapping",
            "bill-ptf_gtv_api_calls",
            "bill-ptf_service_resource_mapping"
    );

    private final List<String> tableNames;

    public DynamoDBHealthCheckContributor(@Value("${ENVIRONMENT}") String env) {
        this.tableNames = BILLING_TABLES.stream()
                .map(tableName -> env + "_" + tableName)
                .collect(toList());
    }
}
