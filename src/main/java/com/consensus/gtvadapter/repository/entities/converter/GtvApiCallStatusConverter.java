package com.consensus.gtvadapter.repository.entities.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.consensus.gtvadapter.repository.entities.GtvApiCallStatus;

import java.util.Optional;

public class GtvApiCallStatusConverter implements DynamoDBTypeConverter<String, GtvApiCallStatus> {

    @Override
    public String convert(GtvApiCallStatus status) {
        return Optional.ofNullable(status).map(GtvApiCallStatus::value).orElse(null);
    }

    @Override
    public GtvApiCallStatus unconvert(String str) {
        return Optional.ofNullable(str).map(GtvApiCallStatus::fromValue).orElse(null);
    }
}
