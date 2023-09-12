package com.consensus.gtvadapter.common.models.event.gtv.request;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationGtvData;
import com.consensus.gtvadapter.common.models.gtv.account.ResponsibleParty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreationGtvRequest extends BaseGtvRequest<AccountCreationGtvData> {

    public static final String TYPE = "customer-gtv-request";

    public AccountCreationGtvRequest() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(body)
                .map(AccountCreationGtvData::getResponsibleParty)
                .map(ResponsibleParty::getExternalCustomerNum)
                .orElse(null);
    }
}
