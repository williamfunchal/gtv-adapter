package com.consensus.gtvadapter.common.models.event;

import java.util.UUID;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DataMappingStoredEvent extends AdapterEvent {

    public static final String TYPE = "data-mapping-saved";

    private IspGtvMapping ispGtvMapping;

    @Builder
    public DataMappingStoredEvent(String eventType, UUID correlationId, IspGtvMapping ispGtvMapping) {
        super(eventType, correlationId);
        this.ispGtvMapping = ispGtvMapping;
    }

}
