package com.consensus.gtvadapter.common.models.gtv.usage;

import com.consensus.gtvadapter.common.models.gtv.GtvData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageEventsBulkRequest implements GtvData {

    private String mode;
    private List<UsageCreationGtvData> usageEvents;
}
