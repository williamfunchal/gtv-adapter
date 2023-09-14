package com.consensus.gtvadapter.repository.entities.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.consensus.gtvadapter.repository.entities.EventStatus;

import java.util.Optional;

public class EventStatusConverter implements DynamoDBTypeConverter<String, EventStatus> {
    @Override
    public String convert(EventStatus status) {
        return Optional.ofNullable(status).map(EventStatus::value).orElse(null);
    }

    @Override
    public EventStatus unconvert(String str) {
        return Optional.ofNullable(str).map(EventStatus::fromValue).orElse(null);
    }
}
