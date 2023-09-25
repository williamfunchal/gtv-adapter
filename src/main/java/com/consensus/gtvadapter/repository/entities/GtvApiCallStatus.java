package com.consensus.gtvadapter.repository.entities;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum GtvApiCallStatus {

    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    ERROR("ERROR");

    private final static Map<String, GtvApiCallStatus> CONSTANTS = new HashMap<>();

    static {
        for (GtvApiCallStatus c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private final String value;

    public String value() {
        return this.value;
    }

    public static GtvApiCallStatus fromValue(String str) {
        return CONSTANTS.get(str);
    }

}
