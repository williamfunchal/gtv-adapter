package com.consensus.gtvadapter.common.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.UUID;
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GtvDataReady {
    private GtvRequest request;
    private UUID correlationId;
}
