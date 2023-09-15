package com.consensus.gtvadapter.processor.service.usage;

import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtvadapter.common.models.event.isp.ready.IspUsageNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.EventBatch;
import com.consensus.gtvadapter.common.models.event.isp.store.UsageStoreEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageCreationGtvData;
import com.consensus.gtvadapter.common.models.rawdata.IspUsageData;
import com.consensus.gtvadapter.processor.service.BatchEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class IspUsageNewEventProcessor implements BatchEventProcessor<IspUsageNewEvent> {
    private final UsageMapper usageMapper;

    @Override
    public UsageStoreEvent process(List<IspUsageNewEvent> ispUsageNewEvents) {
        final List<EventBatch> usageEvents = new ArrayList<>();
        ispUsageNewEvents.forEach(ispUsageNewEvent -> {
            final IspUsageData ispUsageData = ispUsageNewEvent.getData();
            final UsageCreationGtvData usageCreationGtvData = usageMapper.mapToUsageCreationGtvData(ispUsageData);
            final EventBatch eventBatch = EventBatch.builder()
                    .eventId(ispUsageData.getMsgId())
                    .correlationId(ispUsageNewEvent.getCorrelationId())
                    .rawData(ispUsageData)
                    .gtvData(usageCreationGtvData).build();
            usageEvents.add(eventBatch);
        });

        final UsageStoreEvent usageStoreEvent = new UsageStoreEvent();
        usageStoreEvent.setEventId(CCSIUUIDUtils.generateUUID());
        usageStoreEvent.setCorrelationId(CCSIUUIDUtils.generateUUID());
        usageStoreEvent.setData(usageEvents);

        return usageStoreEvent;
    }

    @Override
    public String eventType() {
        return IspUsageNewEvent.TYPE;
    }
}
