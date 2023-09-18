package com.consensus.gtvadapter.repository.mapper;

import com.consensus.gtvadapter.common.models.event.isp.store.CustomerStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.CustomerStoredEvent;
import com.consensus.gtvadapter.common.models.event.isp.update.CustomerUpdateEvent;
import com.consensus.gtvadapter.repository.entities.CustomerDbEvent;
import com.consensus.gtvadapter.repository.entities.EventStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = {EventStatus.class})
public abstract class CustomerEventMapper extends BaseEventMapper {

    @Mapping(target = "rawData", qualifiedByName = "convertToAttributeValue")
    @Mapping(target = "gtvData", qualifiedByName = "convertToAttributeValue")
    @Mapping(target = "status", expression = "java(EventStatus.NEW)")
    public abstract CustomerDbEvent toEventEntity(CustomerStoreEvent storeEvent);

    @Mapping(target = "gtvData", source = "body", qualifiedByName = "convertToAttributeValue")
    @Mapping(target = "status", expression = "java(EventStatus.NEW)")
    @Mapping(target = "tableName", ignore = true)
    @Mapping(target = "operation", ignore = true)
    @Mapping(target = "rawData", ignore = true)
    public abstract CustomerDbEvent toEventEntity(CustomerUpdateEvent updateEvent);

    @Mapping(target = "eventType", ignore = true)
    public abstract CustomerStoredEvent toStoredEvent(CustomerStoreEvent storeEvent);
}
