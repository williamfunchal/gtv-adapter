package com.consensus.gtvadapter.common.models.rawdata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspUsageData implements IspData{
    private String billingDateTime;
    private String phoneNumber;
    private String pages;
    private String duration;
    private String messageId;
    private String customerkey;
    private String resourceType;
    private String currencyCode;
    private String serviceKey;
    private String serviceType;
    private String messageToEmail;
}
