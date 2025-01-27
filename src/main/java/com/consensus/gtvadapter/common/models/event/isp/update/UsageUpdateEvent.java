package com.consensus.gtvadapter.common.models.event.isp.update;

import com.consensus.gtvadapter.common.models.gtv.usage.UsageEventsBulkRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageUpdateEvent extends BaseDataUpdateEvent<UsageEventsBulkRequest> {

    public static final String TYPE = "usage-adapter-update";

    public UsageUpdateEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(eventId).orElse(TYPE);
    }
}
