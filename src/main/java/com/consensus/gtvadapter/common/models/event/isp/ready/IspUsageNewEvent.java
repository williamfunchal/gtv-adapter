package com.consensus.gtvadapter.common.models.event.isp.ready;

import com.consensus.gtvadapter.common.models.rawdata.IspUsageData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IspUsageNewEvent extends BaseIspDataReadyEvent<IspUsageData> {

    public static final String TYPE = "usage-isp-new";

    public IspUsageNewEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(eventId).orElse(TYPE);
    }
}
