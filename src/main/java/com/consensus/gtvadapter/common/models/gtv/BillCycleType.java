package com.consensus.gtvadapter.common.models.gtv;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Getter
@RequiredArgsConstructor
public enum BillCycleType {
    DAILY("daily"),
    MONTHLY("monthly"),
    QUARTERLY("quarterly"),
    YEARLY("yearly"),
    SEGMENTED("segmented-monthly"),
    UNKNOWN("unknown");

    @JsonValue
    private final String value;

    private static final Map<String, BillCycleType> CONSTANTS = new HashMap<>();
    static {
        for (BillCycleType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    public static BillCycleType fromValue(String value) {
        if (isNull(value)) {
            return null;
        }
        return CONSTANTS.getOrDefault(value.toUpperCase(), UNKNOWN);
    }
}
