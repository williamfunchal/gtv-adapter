package com.consensus.gtvadapter.repository.service.usage;

import com.consensus.gtvadapter.common.models.event.UsageAdapterEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.UsageBatchStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.UsageBatchStoredEvent;
import com.consensus.gtvadapter.repository.entities.EventStatus;
import com.consensus.gtvadapter.repository.entities.UsageDbEvent;
import com.consensus.gtvadapter.repository.mapper.UsageEventMapper;
import com.consensus.gtvadapter.repository.service.RepositoryEventProcessor;
import com.consensus.gtvadapter.repository.sqs.DataStoredQueuePublishService;
import com.consensus.gtvadapter.repository.storage.UsageEventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.consensus.gtvadapter.repository.entities.EventStatus.IN_PROGRESS;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
class UsageStoreEventProcessor implements RepositoryEventProcessor<UsageBatchStoreEvent> {

    private final UsageEventsRepository usageEventsRepository;
    private final DataStoredQueuePublishService dataStoredQueuePublishService;
    private final UsageEventMapper usageEventMapper;

    @Override
    public String eventType() {
        return UsageBatchStoreEvent.TYPE;
    }

    @Override
    public void process(UsageBatchStoreEvent storeEvent) {
        List<UsageAdapterEvent> eventBatch = storeEvent.getEventBatch();
        if (isEmpty(eventBatch)) {
            log.warn("Received empty usage event batch. Batch eventId={}", storeEvent.getEventId());
            return;
        }

        // Validate events are absent from DDB or not in IN_PROGRESS or REPLICATED status
        List<UsageAdapterEvent> eventsToStore = filterProcessedEvents(eventBatch);
        if (isEmpty(eventsToStore)) {
            log.warn("All usage events from the batch Id {} have already been processed.", storeEvent.getEventId());
            return;
        }

        // Persist events to DynamoDB with status = NEW;
        List<UsageDbEvent> newUsageDbEvents = eventsToStore.stream()
                .map(usageEventMapper::toEventEntity)
                .collect(toList());
        usageEventsRepository.saveAll(newUsageDbEvents);

        // Publish stored event to the ADAPTER-DATA-STORED queue;
        UsageBatchStoredEvent usageStoredEvent = usageEventMapper.toStoredEvent(storeEvent, eventsToStore);
        dataStoredQueuePublishService.publishMessage(usageStoredEvent);

        // Update events in DynamoDB with IN_PROGRESS status
        newUsageDbEvents.forEach(usageEvent -> usageEvent.setStatus(IN_PROGRESS));
        usageEventsRepository.updateAll(newUsageDbEvents);
    }

    private List<UsageAdapterEvent> filterProcessedEvents(List<UsageAdapterEvent> eventBatchToStore) {
        // Search for events in DB
        Set<String> eventIds = eventBatchToStore.stream().map(UsageAdapterEvent::getEventId).collect(toSet());
        Map<String, UsageDbEvent> usageEventsInDb = usageEventsRepository.findByEventIdIn(eventIds)
                .stream()
                .collect(toMap(UsageDbEvent::getEventId, identity()));
        // Filter out already processed events if any
        List<UsageAdapterEvent> newEventsToStore = new ArrayList<>(eventBatchToStore.size());
        for (UsageAdapterEvent event : eventBatchToStore) {
            String eventId = event.getEventId();
            UsageDbEvent usageDbEvent = usageEventsInDb.get(eventId);
            EventStatus eventStatus = Optional.ofNullable(usageDbEvent)
                    .map(UsageDbEvent::getStatus)
                    .orElse(EventStatus.NEW);
            if (!eventStatus.isNew()) {
                log.warn("Usage event with Id {} has already been received and processed in status: {}", eventId, eventStatus);
                continue;
            }
            newEventsToStore.add(event);
        }
        return newEventsToStore;
    }
}
