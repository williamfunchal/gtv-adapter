package com.consensus.gtvadapter.api.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChargeCategory {
    private Long id;
    private String chargeCategoryType;
    private String name;
}
