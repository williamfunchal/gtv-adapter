package com.consensus.gtvadapter.common.model.gtv;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Getter
@RequiredArgsConstructor
public enum PartyType {
    PERSON("person"),
    ORGANIZATION("organization"),
    UNKNOWN("unknown");

    @JsonValue
    private final String value;

    private static final Map<String, PartyType> CONSTANTS = new HashMap<>();
    static {
        for (PartyType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    public static PartyType fromValue(String value) {
        if (isNull(value)) {
            return null;
        }
        return CONSTANTS.getOrDefault(value.toUpperCase(), UNKNOWN);
    }
}
