package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Getter
@RequiredArgsConstructor
public enum CustomFieldType {
    BILLING_ACCOUNT("billing-account"),
    BILLING_ACCOUNT_CATEGORY("billing-account-category"),
    ORDER("order"),
    SERVICE("service"),
    PAYMENT_METHOD("payment-method"),
    ORDER_ITEM("order-item"),
    PRODUCT("product"),
    UNKNOWN("unknown");

    @JsonValue
    private final String value;

    private static final Map<String, CustomFieldType> CONSTANTS = new HashMap<>();

    static {
        for (CustomFieldType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    public static CustomFieldType fromValue(String value) {
        if (isNull(value)) {
            return null;
        }
        return CONSTANTS.getOrDefault(value.toUpperCase(), UNKNOWN);
    }
}
