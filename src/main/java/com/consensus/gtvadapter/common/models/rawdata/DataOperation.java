package com.consensus.gtvadapter.common.models.rawdata;

import lombok.Getter;

@Getter
public enum DataOperation {
    CREATE("I"),
    UPDATE("U"),
    DELETE("D");

    private final String operation;

    DataOperation(String operation) {
        this.operation = operation;
    }

    public static String getValueString(DataOperation dataOperation) {
        return dataOperation.operation;
    }

    public static DataOperation get(String operation) {
        for (DataOperation dataOperation : DataOperation.values()) {
            if (dataOperation.getOperation().equals(operation)) {
                return dataOperation;
            }
        }
        throw new IllegalArgumentException("Invalid operation: " + operation);
    }
}
