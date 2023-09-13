package com.consensus.gtvadapter.common.models.event.isp.ready;

import com.consensus.gtvadapter.common.models.rawdata.IspCustomerData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspCustomerNewEvent extends BaseIspDataReadyEvent<IspCustomerData> {

    public static final String TYPE = "customer-isp-new";

    public IspCustomerNewEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(data)
                .map(IspCustomerData::getCustomerKey)
                .orElse(TYPE);
    }
}
