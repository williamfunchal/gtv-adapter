package com.consensus.gtvadapter.common.model;

import com.consensus.gtvadapter.common.model.rawdata.IspRawData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspGtvMapping {
    private IspRawData rawData;
    private MappedData mappedData;
    private String correlationId;
}
