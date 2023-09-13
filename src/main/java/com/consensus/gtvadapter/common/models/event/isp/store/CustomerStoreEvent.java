package com.consensus.gtvadapter.common.models.event.isp.store;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationGtvData;
import com.consensus.gtvadapter.common.models.rawdata.IspCustomerData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerStoreEvent extends BaseDataStoreEvent<IspCustomerData, AccountCreationGtvData> {

    public static final String TYPE = "customer-adapter-store";

    public CustomerStoreEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(rawData)
                .map(IspCustomerData::getCustomerKey)
                .orElse(TYPE);
    }
}