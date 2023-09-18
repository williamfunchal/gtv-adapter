package com.consensus.gtvadapter.repository.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public enum EventStatus {

    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    REPLICATED("REPLICATED");

    private final static Map<String, EventStatus> CONSTANTS = new HashMap<>();
    static {
        for (EventStatus c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private final String value;

    public String value() {
        return this.value;
    }

    public boolean isNew() {
        return this == NEW;
    }

    public boolean isInProgress() {
        return this == IN_PROGRESS;
    }

    public boolean isReplicated() {
        return this == REPLICATED;
    }

    public static EventStatus fromValue(String str) {
        return CONSTANTS.get(str);
    }

}