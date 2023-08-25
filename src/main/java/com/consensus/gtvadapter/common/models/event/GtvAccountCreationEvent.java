package com.consensus.gtvadapter.common.models.event;

import java.util.UUID;

import org.springframework.http.HttpMethod;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GtvAccountCreationEvent extends AdapterEvent {

    public static final String TYPE = "gtv-account-creation";

    private HttpMethod method;
    private String api;
    private AccountCreationRequestBody body;

    @Builder
    public GtvAccountCreationEvent(String eventType, UUID correlationId, HttpMethod method, String api, AccountCreationRequestBody body) {
        super(eventType, correlationId);
        this.method = method;
        this.api = api;
        this.body = body;
    }
}
