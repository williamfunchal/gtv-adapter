package com.consensus.gtvadapter.common.models.gtv.usage;

import com.consensus.gtvadapter.common.models.gtv.GtvData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageCreationGtvData implements GtvData {

    private String serviceResourceIdentifier;
    private ZonedDateTime startTime;
    private UsageUom usageUom;
    private Integer usageAmount;
    private String description;
    private ZonedDateTime endTime;
    private String referenceId;
    private String sequenceId;
    private String text01;
    private String text02;
    private String text03;
    private String text04;
    private String text05;
    private Long number01;
}