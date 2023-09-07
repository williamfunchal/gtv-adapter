package com.consensus.gtvadapter.common.models.rawdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public enum DataOperation {

    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private static final Map<String, DataOperation> CONSTANTS = new HashMap<>();

    static {
        for (DataOperation c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private final String value;

    DataOperation(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static DataOperation fromValue(String value) {
        return Optional.ofNullable(CONSTANTS.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported operation exception: " + value));
    }
}