package com.consensus.gtvadapter.repository.service;

import com.consensus.gtvadapter.common.models.event.isp.store.CustomerStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.CustomerStoredEvent;
import com.consensus.gtvadapter.repository.entities.CustomerDbEvent;
import com.consensus.gtvadapter.repository.entities.EventStatus;
import com.consensus.gtvadapter.repository.mapper.CustomerEventMapper;
import com.consensus.gtvadapter.repository.sqs.DataStoredQueuePublishService;
import com.consensus.gtvadapter.repository.storage.CustomerEventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.consensus.gtvadapter.repository.entities.EventStatus.IN_PROGRESS;

@Slf4j
@Service
@RequiredArgsConstructor
class CustomerStoreEventProcessor implements RepositoryEventProcessor<CustomerStoreEvent> {

    private final CustomerEventsRepository customerEventsRepository;
    private final DataStoredQueuePublishService dataStoredQueuePublishService;
    private final CustomerEventMapper customerEventMapper;

    @Override
    public String eventType() {
        return CustomerStoreEvent.TYPE;
    }

    @Override
    public void process(CustomerStoreEvent storeEvent) {
        // Validate event is absent from DDB or not in IN_PROGRESS or REPLICATED status
        String eventId = storeEvent.getEventId();
        CustomerDbEvent existingEvent = customerEventsRepository.findByEventId(eventId);
        EventStatus eventStatus = Optional.ofNullable(existingEvent)
                .map(CustomerDbEvent::getStatus)
                .orElse(EventStatus.NEW);
        if (!eventStatus.isNew()) {
            log.warn("Customer event with Id {} has already been received and processed in status: {}", eventId, eventStatus);
            return;
        }

        // Persist event to DynamoDB with status = NEW;
        CustomerDbEvent newEvent = customerEventMapper.toEventEntity(storeEvent);
        customerEventsRepository.save(newEvent);

        // Publish stored event to the ADAPTER-DATA-STORED queue;
        CustomerStoredEvent storedEvent = customerEventMapper.toStoredEvent(storeEvent);
        dataStoredQueuePublishService.publishMessage(storedEvent);

        // Update event in DynamoDB with IN_PROGRESS status
        newEvent.setStatus(IN_PROGRESS);
        customerEventsRepository.save(newEvent);
    }
}
