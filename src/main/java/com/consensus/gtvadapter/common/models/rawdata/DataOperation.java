package com.consensus.gtvadapter.common.models.rawdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum DataOperation {

    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    UNKNOWN("UNKNOWN");

    private static final Map<String, DataOperation> CONSTANTS = new HashMap<>();

    static {
        for (DataOperation c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private final String value;

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static DataOperation fromValue(String value) {
        return CONSTANTS.getOrDefault(value, UNKNOWN);
    }
}