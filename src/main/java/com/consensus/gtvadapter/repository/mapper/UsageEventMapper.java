package com.consensus.gtvadapter.repository.mapper;

import com.consensus.gtvadapter.common.models.event.UsageAdapterEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.UsageBatchStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.UsageBatchStoredEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageCreationGtvData;
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

    @Mapping(target = "eventId", source = "event.referenceId")
    @Mapping(target = "correlationId", source = "correlationId")
    @Mapping(target = "gtvData", source = "event", qualifiedByName = "convertToAttributeValue")
    @Mapping(target = "status", expression = "java(EventStatus.NEW)")
    @Mapping(target = "tableName", ignore = true)
    @Mapping(target = "operation", ignore = true)
    @Mapping(target = "rawData", ignore = true)
    public abstract UsageDbEvent toEventEntity(UsageCreationGtvData event, String correlationId);

    @Mapping(target = "eventBatch", source = "eventBatch")
    @Mapping(target = "eventType", ignore = true)
    public abstract UsageBatchStoredEvent toStoredEvent(UsageBatchStoreEvent storeEvent, List<UsageAdapterEvent> eventBatch);
}
