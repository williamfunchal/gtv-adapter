package com.consensus.gtvadapter.common.models.event.isp.update;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationGtvData;
import com.consensus.gtvadapter.common.models.gtv.account.ResponsibleParty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerUpdateEvent extends BaseDataUpdateEvent<AccountCreationGtvData> {

    public static final String TYPE = "customer-adapter-update";

    public CustomerUpdateEvent() {
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
