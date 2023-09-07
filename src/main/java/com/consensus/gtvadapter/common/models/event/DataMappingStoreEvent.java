package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DataMappingStoreEvent extends AdapterEvent {

    public static final String TYPE = "save-data-mapping";

    private IspGtvMapping ispGtvMapping;

    public DataMappingStoreEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return TYPE;
    }
}
