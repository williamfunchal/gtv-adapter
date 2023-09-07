package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.rawdata.IspCustomerData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspNewCustomerEvent extends IspDataReadyEvent<IspCustomerData> {

    public static final String TYPE = "isp-new-customer";

    public IspNewCustomerEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(data)
                .map(IspCustomerData::getCustomerkey)
                .orElse(TYPE);
    }
}
