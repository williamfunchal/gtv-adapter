package com.consensus.gtvadapter.common.models.response;

import org.springframework.http.HttpMethod;

import com.consensus.gtvadapter.common.models.request.GtvRequestAccountCreation;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "request_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GtvRequestAccountCreation.class, name = "account-creation-response")
})
public abstract class GtvResponse {
    private HttpMethod method;
    private String api;
    
}
