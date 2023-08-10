package com.consensus.gtvadapter.common.models;

import com.consensus.gtvadapter.common.models.request.GtvRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MappedData {
    private List<GtvRequest> requests;
}
