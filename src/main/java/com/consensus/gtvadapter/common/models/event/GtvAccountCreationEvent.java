package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GtvAccountCreationEvent extends AdapterEvent {

    public static final String TYPE = "gtv-account-creation";

    private HttpMethod method;
    private String api;
    private AccountCreationRequestBody body;

    public GtvAccountCreationEvent() {
        this.eventType = TYPE;
    }
}
