package com.consensus.gtvadapter.common.models.rawdata;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.UUID;

@SuperBuilder
@Data 
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IspRawDataCustomer.class, name = "customer")
})
public abstract class IspRawData {
    private UUID correlationId;
    private String tableName;
    private DataOperation operation;
}
