package com.consensus.gtvadapter.common.model;

import java.util.List;

import com.consensus.gtvadapter.common.model.request.GtvRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MappedData {
    private List<GtvRequest> requests;
}
