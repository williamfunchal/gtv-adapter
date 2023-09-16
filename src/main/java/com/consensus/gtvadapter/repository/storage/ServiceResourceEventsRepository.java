package com.consensus.gtvadapter.repository.storage;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceResourceEventsRepository {

    public static final String SERVICE_RESOURCE_EVENTS_TABLE = "service_resource_events";

    private final DynamoDB dynamoDB;
    private final ObjectMapper objectMapper;
    private final String tableName;

    public ServiceResourceEventsRepository(DynamoDB dynamoDB, ObjectMapper objectMapper,
            @Value("${ENVIRONMENT}") String env, @Value("${NAMESPACE}") String namespace) {
        this.dynamoDB = dynamoDB;
        this.objectMapper = objectMapper;
        this.tableName = env + "_" + namespace + "_" + SERVICE_RESOURCE_EVENTS_TABLE;
    }
}
