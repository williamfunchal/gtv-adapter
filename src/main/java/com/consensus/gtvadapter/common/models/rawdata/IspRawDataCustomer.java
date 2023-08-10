package com.consensus.gtvadapter.common.models.rawdata;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeName("customer")
public class IspRawDataCustomer extends IspRawData{
    private IspCustumerData data;
}
