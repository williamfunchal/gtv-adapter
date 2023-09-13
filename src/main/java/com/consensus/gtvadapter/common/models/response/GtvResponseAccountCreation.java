package com.consensus.gtvadapter.common.models.response;

import com.consensus.gtvadapter.common.models.request.GtvRequest;
import com.consensus.gtvadapter.common.models.request.GtvRequestAccountCreation;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GtvResponseAccountCreation extends GtvResponse{
    GtvRequestAccountCreation request;
}
