package com.consensus.gtvadapter.repository.storage;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsageEventsRepository {

    public static final String USAGE_EVENTS_TABLE = "usage_events";

    private final DynamoDB dynamoDB;
    private final ObjectMapper objectMapper;
    private final String tableName;

    public UsageEventsRepository(DynamoDB dynamoDB, ObjectMapper objectMapper,
            @Value("${ENVIRONMENT}") String env, @Value("${NAMESPACE}") String namespace) {
        this.dynamoDB = dynamoDB;
        this.objectMapper = objectMapper;
        this.tableName = env + "_" + namespace + "_" + USAGE_EVENTS_TABLE;
    }

    @SneakyThrows
    private String convertToJson(IspGtvMapping ispGtvMapping) {
        return objectMapper.writeValueAsString(ispGtvMapping);
    }
}
