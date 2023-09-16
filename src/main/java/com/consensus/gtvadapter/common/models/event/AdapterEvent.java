package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.event.gtv.request.AccountCreationGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.response.AccountCreationGtvResponse;
import com.consensus.gtvadapter.common.models.event.isp.ready.IspCustomerNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.ready.IspUsageNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.CustomerStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.UsageBatchStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.CustomerStoredEvent;
import com.consensus.gtvadapter.common.models.event.isp.update.CustomerUpdateEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "event_type", visible = true)
@JsonSubTypes({
        // New Customer Event Flow
        @JsonSubTypes.Type(value = IspCustomerNewEvent.class, name = IspCustomerNewEvent.TYPE),
        @JsonSubTypes.Type(value = CustomerStoreEvent.class, name = CustomerStoreEvent.TYPE),
        @JsonSubTypes.Type(value = CustomerStoredEvent.class, name = CustomerStoredEvent.TYPE),
        @JsonSubTypes.Type(value = AccountCreationGtvRequest.class, name = AccountCreationGtvRequest.TYPE),
        @JsonSubTypes.Type(value = AccountCreationGtvResponse.class, name = AccountCreationGtvResponse.TYPE),
        @JsonSubTypes.Type(value = CustomerUpdateEvent.class, name = CustomerUpdateEvent.TYPE),

        // New Usage Event Flow
        @JsonSubTypes.Type(value = IspUsageNewEvent.class, name = IspUsageNewEvent.TYPE),
        @JsonSubTypes.Type(value = UsageBatchStoreEvent.class, name = UsageBatchStoreEvent.TYPE),
        @JsonSubTypes.Type(value = UsageAdapterEvent.class, name = UsageAdapterEvent.TYPE),

})
public abstract class AdapterEvent {

    protected String eventId;
    protected String eventType;
    protected String correlationId;

    @JsonIgnore
    public abstract String getGroupId();
}
