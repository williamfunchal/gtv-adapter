package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpMethod;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreationResultsEvent extends AdapterEvent {

    public static final String TYPE = "account-creation-result";

    private HttpMethod method;
    private String api;
    private AccountCreationRequestBody body;
    private GtvRequestDetails result;

    @Builder
    public AccountCreationResultsEvent(String eventType, UUID correlationId, HttpMethod method, String api, AccountCreationRequestBody body, GtvRequestDetails result) {
        super(eventType, correlationId);
        this.method = method;
        this.api = api;
        this.body = body;
        this.result = result;
    }
}
