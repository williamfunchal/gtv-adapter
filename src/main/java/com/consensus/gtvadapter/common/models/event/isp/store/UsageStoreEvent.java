package com.consensus.gtvadapter.common.models.event.isp.store;

import com.consensus.gtvadapter.common.models.event.isp.ready.IspUsageNewEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageCreationGtvData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageStoreEvent extends BatchDataStoreEvent<IspUsageNewEvent, UsageCreationGtvData>{
    public static final String TYPE = "usage-adapter-store";

    public UsageStoreEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(eventId)
                .orElse(TYPE);
    }
}
