package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreationResultsEvent extends ResultsEvent {

    public static final String TYPE = "account-creation-result";

    private AccountCreationRequestBody body;

    public AccountCreationResultsEvent() {
        this.eventType = TYPE;
    }
}
