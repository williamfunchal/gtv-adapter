package com.consensus.gtvadapter.repository.mapper;

import com.consensus.gtvadapter.common.models.event.UsageAdapterEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.CustomerStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.UsageBatchStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.CustomerStoredEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.UsageBatchStoredEvent;
import com.consensus.gtvadapter.common.models.event.isp.update.CustomerUpdateEvent;
import com.consensus.gtvadapter.repository.entities.CustomerDbEvent;
import com.consensus.gtvadapter.repository.entities.EventStatus;
import com.consensus.gtvadapter.repository.entities.UsageDbEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(imports = {EventStatus.class})
public abstract class UsageEventMapper extends BaseEventMapper {

    @Mapping(target = "rawData", qualifiedByName = "convertToAttributeValue")
    @Mapping(target = "gtvData", qualifiedByName = "convertToAttributeValue")
    @Mapping(target = "status", expression = "java(EventStatus.NEW)")
    public abstract UsageDbEvent toEventEntity(UsageAdapterEvent usageEvent);


    @Mapping(target = "eventBatch", source = "eventBatch")
    @Mapping(target = "eventType", ignore = true)
    public abstract UsageBatchStoredEvent toStoredEvent(UsageBatchStoreEvent storeEvent, List<UsageAdapterEvent> eventBatch);
}
