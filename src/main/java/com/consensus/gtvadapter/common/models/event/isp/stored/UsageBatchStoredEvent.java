package com.consensus.gtvadapter.common.models.event.isp.stored;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.UsageAdapterEvent;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageBatchStoredEvent extends AdapterEvent {

    public static final String TYPE = "usage-adapter-stored";

    private List<UsageAdapterEvent> eventBatch;

    public UsageBatchStoredEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(eventId).orElse(TYPE);
    }
}