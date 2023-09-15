package com.consensus.gtvadapter.common.models.event.isp.stored;

import com.consensus.gtvadapter.common.models.event.isp.store.BaseDataStoreEvent;
import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationGtvData;
import com.consensus.gtvadapter.common.models.rawdata.CustomerIspData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerStoredEvent extends BaseDataStoreEvent<CustomerIspData, AccountCreationGtvData> {

    public static final String TYPE = "customer-adapter-stored";

    public CustomerStoredEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(rawData)
                .map(CustomerIspData::getCustomerKey)
                .orElse(TYPE);
    }
}