package com.consensus.gtvadapter.repository.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.consensus.gtvadapter.common.models.rawdata.DataOperation;
import com.consensus.gtvadapter.repository.entities.converter.DataOperationConverter;
import com.consensus.gtvadapter.repository.entities.converter.EventStatusConverter;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = UsageDbEvent.TABLE_NAME)
public class UsageDbEvent {

    public static final String TABLE_NAME = "usage_events";

    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "event_id")
    private String eventId;

    @DynamoDBAttribute(attributeName = "correlation_id")
    private String correlationId;

    @DynamoDBAttribute(attributeName = "table_name")
    private String tableName;

    @DynamoDBTypeConverted(converter = DataOperationConverter.class)
    @DynamoDBAttribute(attributeName = "operation")
    private DataOperation operation;

    @DynamoDBTypeConverted(converter = EventStatusConverter.class)
    @DynamoDBAttribute(attributeName = "status")
    private EventStatus status;

    @DynamoDBAttribute(attributeName = "raw_data")
    private AttributeValue rawData;

    @DynamoDBAttribute(attributeName = "gtv_data")
    private AttributeValue gtvData;
}
