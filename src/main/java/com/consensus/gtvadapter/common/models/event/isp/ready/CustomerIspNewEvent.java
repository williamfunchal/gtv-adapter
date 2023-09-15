package com.consensus.gtvadapter.common.models.event.isp.ready;

import com.consensus.gtvadapter.common.models.rawdata.CustomerIspData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerIspNewEvent extends BaseIspDataReadyEvent<CustomerIspData> {

    public static final String TYPE = "customer-isp-new";

    public CustomerIspNewEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(data)
                .map(CustomerIspData::getCustomerKey)
                .orElse(TYPE);
    }
}
