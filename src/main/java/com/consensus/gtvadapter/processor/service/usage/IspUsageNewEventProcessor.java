package com.consensus.gtvadapter.processor.service.usage;

import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtvadapter.common.models.event.UsageAdapterEvent;
import com.consensus.gtvadapter.common.models.event.isp.ready.IspUsageNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.UsageBatchStoreEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageCreationGtvData;
import com.consensus.gtvadapter.common.models.rawdata.IspUsageData;
import com.consensus.gtvadapter.processor.service.BatchEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class IspUsageNewEventProcessor implements BatchEventProcessor<IspUsageNewEvent> {

    private final UsageMapper usageMapper;

    @Override
    public UsageBatchStoreEvent process(List<IspUsageNewEvent> ispUsageNewEvents) {
        List<UsageAdapterEvent> usageEvents = ispUsageNewEvents.stream()
                .map(this::createUsageAdapterEvent)
                .collect(toList());

        UsageBatchStoreEvent usageBatchStoreEvent = new UsageBatchStoreEvent();
        usageBatchStoreEvent.setEventId(CCSIUUIDUtils.generateUUID());
        usageBatchStoreEvent.setCorrelationId(CCSIUUIDUtils.generateUUID());
        usageBatchStoreEvent.setEventBatch(usageEvents);

        return usageBatchStoreEvent;
    }

    private UsageAdapterEvent createUsageAdapterEvent(IspUsageNewEvent ispUsageNewEvent) {
        IspUsageData ispUsageData = ispUsageNewEvent.getData();
        UsageCreationGtvData usageCreationGtvData = usageMapper.mapToUsageCreationGtvData(ispUsageData);

        UsageAdapterEvent usageAdapterEvent = new UsageAdapterEvent();
        usageAdapterEvent.setEventId(ispUsageNewEvent.getEventId());
        usageAdapterEvent.setCorrelationId(ispUsageNewEvent.getCorrelationId());
        usageAdapterEvent.setOperation(ispUsageNewEvent.getOperation());
        usageAdapterEvent.setTableName(ispUsageNewEvent.getTableName());
        usageAdapterEvent.setRawData(ispUsageData);
        usageAdapterEvent.setGtvData(usageCreationGtvData);

        return usageAdapterEvent;
    }

    @Override
    public String eventType() {
        return IspUsageNewEvent.TYPE;
    }
}
