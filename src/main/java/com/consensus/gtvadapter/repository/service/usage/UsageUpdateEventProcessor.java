package com.consensus.gtvadapter.repository.service.usage;

import com.consensus.gtvadapter.common.models.event.isp.update.UsageUpdateEvent;
import com.consensus.gtvadapter.repository.mapper.UsageEventMapper;
import com.consensus.gtvadapter.repository.service.RepositoryEventProcessor;
import com.consensus.gtvadapter.repository.storage.UsageEventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class UsageUpdateEventProcessor implements RepositoryEventProcessor<UsageUpdateEvent> {

    private final UsageEventsRepository usageEventsRepository;
    private final UsageEventMapper usageEventMapper;

    @Override
    public String eventType() {
        return UsageUpdateEvent.TYPE;
    }

    @Override
    public void process(UsageUpdateEvent updateEvent) {
        //TODO implement 'eventId' can be taken from 'UsageCreationGtvData.referenceId'
    }
}
