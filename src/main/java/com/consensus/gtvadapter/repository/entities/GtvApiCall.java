package com.consensus.gtvadapter.repository.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.consensus.gtvadapter.repository.entities.converter.GtvApiCallStatusConverter;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamoDBTable(tableName = GtvApiCall.TABLE_NAME)
public class GtvApiCall {

    public static final String TABLE_NAME = "gtv_api_calls";

    @EqualsAndHashCode.Include
    @DynamoDBHashKey(attributeName = "correlation_id")
    private String correlationId;

    @DynamoDBAttribute(attributeName = "method")
    private String method;

    @DynamoDBAttribute(attributeName = "api")
    private String api;

    @DynamoDBTypeConverted(converter = GtvApiCallStatusConverter.class)
    @DynamoDBAttribute(attributeName = "status")
    private GtvApiCallStatus status;

    @DynamoDBAttribute(attributeName = "body")
    private AttributeValue body;

    @DynamoDBAttribute(attributeName = "result")
    private GtvApiResponse result;
}
