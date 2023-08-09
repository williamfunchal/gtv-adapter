package com.consensus.gtvadapter.common.models.gtv;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BillCycle {
    private String id;
    private BillCycleType billCycleType;
}