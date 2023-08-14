package com.consensus.gtvadapter.repository;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.config.DynamoDBProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class DataMappingRepository {

    private final DynamoDB dynamoDB;
    private final ObjectMapper objectMapper;
    private final DynamoDBProperties dynamoDBProperties;

    public void saveDataMapping(IspGtvMapping ispGtvMapping){
        final Table table = dynamoDB.getTable(dynamoDBProperties.getDataMappingTable());
        final String value = convertToJson(ispGtvMapping);
        final Item item = Item.fromJSON(value);
        table.putItem(item);
    }

    @SneakyThrows
    private String convertToJson(IspGtvMapping ispGtvMapping){
        return objectMapper.writeValueAsString(ispGtvMapping);
    }
}
