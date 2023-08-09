package com.consensus.gtvadapter.module.gateway.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RatedEvent extends UsageEvent{
    private Long totalCharge;
    private Integer overwriteCounter;
    private String requestId;
    private Map<String, String> servicePeriod;
    private List<EventCharge> eventCharges;
    private String overwrittenEventId;
}
