package com.consensus.gtvadapter.common.models.gtv.usage;

import com.consensus.gtvadapter.common.models.gtv.usage.ErredEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.RatedEvent;
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