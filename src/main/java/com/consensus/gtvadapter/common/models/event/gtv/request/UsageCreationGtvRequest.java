package com.consensus.gtvadapter.common.models.event.gtv.request;

import com.consensus.gtvadapter.common.models.gtv.usage.UsageEventsBulkRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageCreationGtvRequest extends BaseGtvRequest<UsageEventsBulkRequest> {

    public static final String TYPE = "usage-gtv-request";

    public UsageCreationGtvRequest() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(eventId).orElse(TYPE);
    }
}
