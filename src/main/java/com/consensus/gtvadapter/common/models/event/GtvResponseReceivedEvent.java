package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.models.request.GtvRequestAccountCreation;
import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GtvResponseReceivedEvent extends AdapterEvent {

    public static final String TYPE = "account-creation-response";

    private GtvRequestAccountCreation request;
    private GtvRequestDetails result;

    public GtvResponseReceivedEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return TYPE;
    }
}
