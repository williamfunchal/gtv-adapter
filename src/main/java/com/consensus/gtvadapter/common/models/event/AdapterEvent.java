package com.consensus.gtvadapter.common.models.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "event_type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataMappingStoreEvent.class, name = DataMappingStoreEvent.TYPE)
})
public abstract class AdapterEvent {
    private String eventType;
    private UUID correlationId;
}
