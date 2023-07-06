package com.consensus.gtvadapter.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventCharge {
    private Long id;
    private UsageUom usageUom;
    private Long usageAmount;
    private Map<String, String> usageRule;
    private Long charge;
    private ChargeCategory chargeCategory;
    private Boolean consumePrepaidBalance;
    private Map<String, String> service;
}
