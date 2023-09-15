package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.event.isp.store.BaseDataStoreEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageGtvData;
import com.consensus.gtvadapter.common.models.rawdata.UsageIspData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Optional;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageAdapterEvent extends BaseDataStoreEvent<UsageIspData, UsageGtvData> {

    public static final String TYPE = "usage-event";

    public UsageAdapterEvent() {
        this.eventType = TYPE;
    }

    @Override
    public String getGroupId() {
        return Optional.ofNullable(eventId).orElse(TYPE);
    }
}
