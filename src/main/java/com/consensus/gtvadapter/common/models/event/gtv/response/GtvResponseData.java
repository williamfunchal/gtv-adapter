package com.consensus.gtvadapter.common.models.event.gtv.response;

import lombok.Data;

import java.time.Instant;

@Data
public class GtvResponseData {

    private Instant requestDateTime;
    private Instant responseDateTime;
    private int statusCode;
    private String payload;
}
