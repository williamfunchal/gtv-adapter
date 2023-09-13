package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Getter
@RequiredArgsConstructor
public enum AddressType {

    POSTAL("postal"),
    EMAIL("email"),
    TELECOM("telecom"),
    UNKNOWN("unknown");

    @JsonValue
    private final String value;

    private static final Map<String, AddressType> CONSTANTS = new HashMap<>();

    static {
        for (AddressType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    public static AddressType fromValue(String value) {
        if (isNull(value)) {
            return null;
        }
        return CONSTANTS.getOrDefault(value.toUpperCase(), UNKNOWN);
    }
}
