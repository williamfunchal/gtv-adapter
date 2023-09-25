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
        String batchEventId = CCSIUUIDUtils.generateUUID();
        String batchCorrelationId = CCSIUUIDUtils.generateUUID();

        List<UsageAdapterEvent> usageEvents = ispUsageNewEvents.stream()
                .map(event -> createUsageAdapterEvent(event, batchCorrelationId))
                .collect(toList());

        UsageBatchStoreEvent usageBatchStoreEvent = new UsageBatchStoreEvent();
        usageBatchStoreEvent.setEventId(batchEventId);
        usageBatchStoreEvent.setCorrelationId(batchCorrelationId);
        usageBatchStoreEvent.setEventBatch(usageEvents);

        return usageBatchStoreEvent;
    }

    private UsageAdapterEvent createUsageAdapterEvent(IspUsageNewEvent ispUsageNewEvent, String correlationId) {
        IspUsageData ispUsageData = ispUsageNewEvent.getData();
        UsageCreationGtvData usageCreationGtvData = usageMapper.toGtvData(ispUsageData);

        UsageAdapterEvent usageAdapterEvent = new UsageAdapterEvent();
        usageAdapterEvent.setEventId(ispUsageNewEvent.getEventId());
        usageAdapterEvent.setCorrelationId(correlationId);
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
