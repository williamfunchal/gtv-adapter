package com.consensus.gtvadapter.common.models.event.gtv.response;

import com.consensus.gtvadapter.common.models.gtv.usage.UsageEventsBulkRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageCreationGtvResponse extends BaseGtvResponse<UsageEventsBulkRequest> {

    public static final String TYPE = "usage-gtv-response";

    public UsageCreationGtvResponse() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(eventId).orElse(TYPE);
    }
}
