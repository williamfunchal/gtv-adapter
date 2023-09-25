package com.consensus.gtvadapter.repository.service.usage;

import com.consensus.gtvadapter.common.models.event.isp.update.UsageUpdateEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageCreationGtvData;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageEventsBulkRequest;
import com.consensus.gtvadapter.repository.entities.GtvApiCall;
import com.consensus.gtvadapter.repository.entities.UsageDbEvent;
import com.consensus.gtvadapter.repository.mapper.GtvApiCallMapper;
import com.consensus.gtvadapter.repository.mapper.UsageEventMapper;
import com.consensus.gtvadapter.repository.service.RepositoryEventProcessor;
import com.consensus.gtvadapter.repository.storage.GtvApiCallsRepository;
import com.consensus.gtvadapter.repository.storage.UsageEventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.consensus.gtvadapter.repository.entities.EventStatus.REPLICATED;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
class UsageUpdateEventProcessor implements RepositoryEventProcessor<UsageUpdateEvent> {

    private final UsageEventsRepository usageEventsRepository;
    private final UsageEventMapper usageEventMapper;
    private final GtvApiCallsRepository gtvApiCallsRepository;
    private final GtvApiCallMapper gtvApiCallMapper;

    @Override
    public String eventType() {
        return UsageUpdateEvent.TYPE;
    }

    @Override
    public void process(UsageUpdateEvent updateEvent) {
        List<UsageCreationGtvData> usageCreationEvents = Optional.ofNullable(updateEvent.getBody())
                .map(UsageEventsBulkRequest::getUsageEvents)
                .orElse(emptyList());
        if (isEmpty(usageCreationEvents)) {
            log.error("Received empty usage update event. Update eventId={}", updateEvent.getEventId());
            return;
        }
        Set<String> eventIds = usageCreationEvents.stream()
                .map(UsageCreationGtvData::getReferenceId)
                .collect(toSet());

        Map<String, UsageDbEvent> usageEventsInDb = usageEventsRepository.findByEventIdIn(eventIds)
                .stream()
                .collect(toMap(UsageDbEvent::getEventId, identity()));

        List<UsageDbEvent> dbEventsToUpdate = new ArrayList<>(eventIds.size());
        for (UsageCreationGtvData event : usageCreationEvents) {
            String eventId = event.getReferenceId();
            UsageDbEvent usageDbEvent = usageEventsInDb.get(eventId);
            if (usageDbEvent == null) {
                log.warn("Usage event with Id {} is missing from DB. Creating DB record.", eventId);
                usageDbEvent = usageEventMapper.toEventEntity(event, updateEvent.getCorrelationId());
            }
            usageDbEvent.setStatus(REPLICATED);
            dbEventsToUpdate.add(usageDbEvent);
        }
        usageEventsRepository.saveAll(dbEventsToUpdate);

        GtvApiCall gtvApiCall = gtvApiCallMapper.toGtvApiCall(updateEvent);
        gtvApiCallsRepository.save(gtvApiCall);
    }
}
