package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreationResultsEvent extends AdapterEvent {

    public static final String TYPE = "account-creation-result";

    private HttpMethod method;
    private String api;
    private AccountCreationRequestBody body;
    private GtvRequestDetails result;

    public AccountCreationResultsEvent() {
        this.eventType = TYPE;
    }
}
