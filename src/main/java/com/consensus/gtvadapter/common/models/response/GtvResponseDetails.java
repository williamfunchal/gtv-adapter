package com.consensus.gtvadapter.common.models.response;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class GtvResponseDetails {
    private Instant requestDateTime;
    private Instant responseDateTime;
    private Integer statusCode;
    private JsonNode payload;
}
