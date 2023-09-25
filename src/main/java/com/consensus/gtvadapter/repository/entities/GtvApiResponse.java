package com.consensus.gtvadapter.repository.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.consensus.common.dynamo.db.marshaller.Instant2IsoDynamoDBMarshaller;
import lombok.Data;

import java.time.Instant;

@Data
@DynamoDBDocument
public class GtvApiResponse {

    @DynamoDBTypeConverted(converter = Instant2IsoDynamoDBMarshaller.class)
    @DynamoDBAttribute(attributeName = "request_date")
    private Instant requestDateTime;

    @DynamoDBTypeConverted(converter = Instant2IsoDynamoDBMarshaller.class)
    @DynamoDBAttribute(attributeName = "response_date")
    private Instant responseDateTime;

    @DynamoDBAttribute(attributeName = "status_code")
    private int statusCode;

    @DynamoDBAttribute(attributeName = "payload")
    private String payload;
}
