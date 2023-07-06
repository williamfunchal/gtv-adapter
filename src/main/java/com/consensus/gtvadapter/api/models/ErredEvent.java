package com.consensus.gtvadapter.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErredEvent {
    private String requestId;
    private UsageEvent unratedUsageEvent;
    private String message;
    private String code;
    private String voidedEventId;
}
