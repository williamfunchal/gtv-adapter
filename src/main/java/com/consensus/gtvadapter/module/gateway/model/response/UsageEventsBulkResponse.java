package com.consensus.gtvadapter.module.gateway.model.response;

import java.util.List;

import com.consensus.gtvadapter.module.gateway.model.ErredEvent;
import com.consensus.gtvadapter.module.gateway.model.RatedEvent;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageEventsBulkResponse {
    private List<RatedEvent> ratedEvents;
    private List<ErredEvent> erredEvents;
}
