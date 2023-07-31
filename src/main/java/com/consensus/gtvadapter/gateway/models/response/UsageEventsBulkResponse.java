package com.consensus.gtvadapter.gateway.models.response;

import com.consensus.gtvadapter.gateway.models.ErredEvent;
import com.consensus.gtvadapter.gateway.models.RatedEvent;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageEventsBulkResponse {
    private List<RatedEvent> ratedEvents;
    private List<ErredEvent> erredEvents;
}
