package com.consensus.gtvadapter.repository.storage;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerEventsRepository {

    public static final String CUSTOMER_EVENTS_TABLE = "customer_events";

    private final DynamoDB dynamoDB;
    private final ObjectMapper objectMapper;
    private final String tableName;

    public CustomerEventsRepository(DynamoDB dynamoDB, ObjectMapper objectMapper,
            @Value("${ENVIRONMENT}") String env, @Value("${NAMESPACE}") String namespace) {
        this.dynamoDB = dynamoDB;
        this.objectMapper = objectMapper;
        this.tableName = env + "_" + namespace + "_" + CUSTOMER_EVENTS_TABLE;
    }

    public void save(IspGtvMapping ispGtvMapping){
        final Table table = dynamoDB.getTable(tableName);
        final String value = convertToJson(ispGtvMapping);
        final Item item = Item.fromJSON(value);
        table.putItem(item);
    }

    @SneakyThrows
    private String convertToJson(IspGtvMapping ispGtvMapping){
        return objectMapper.writeValueAsString(ispGtvMapping);
    }
}
