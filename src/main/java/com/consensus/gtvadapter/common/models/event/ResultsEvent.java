package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class ResultsEvent extends AdapterEvent {

    private HttpMethod method;
    private String api;
    private GtvRequestDetails result;
}
