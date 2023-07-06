package com.consensus.gtvadapter.api.models.request;

import com.consensus.gtvadapter.api.models.UsageEvent;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageEventsBulkRequest {
    private String mode;
    private List<UsageEvent> usageEvents;
}
