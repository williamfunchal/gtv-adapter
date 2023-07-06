package com.consensus.gtvadapter.api.models.response;

import com.consensus.gtvadapter.api.models.ErredEvent;
import com.consensus.gtvadapter.api.models.RatedEvent;
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
