package com.consensus.gtvadapter.common.models.dto;

import java.util.UUID;

import com.consensus.gtvadapter.common.models.enums.DataOperation;
import com.consensus.gtvadapter.common.models.enums.DataType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
public class IspRawDTO<T> {
    private UUID correlationId;
    private String tableName;
    private DataOperation operation;
    private DataType dataType;
    private T data;
}