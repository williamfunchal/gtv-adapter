package com.consensus.gtvadapter.repository.service.customer;

import com.consensus.gtvadapter.common.models.event.gtv.response.GtvResponseData;
import com.consensus.gtvadapter.common.models.event.isp.update.CustomerUpdateEvent;
import com.consensus.gtvadapter.repository.entities.CustomerDbEvent;
import com.consensus.gtvadapter.repository.entities.GtvApiCall;
import com.consensus.gtvadapter.repository.mapper.CustomerEventMapper;
import com.consensus.gtvadapter.repository.mapper.GtvApiCallMapper;
import com.consensus.gtvadapter.repository.service.RepositoryEventProcessor;
import com.consensus.gtvadapter.repository.storage.CustomerEventsRepository;
import com.consensus.gtvadapter.repository.storage.GtvApiCallsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.consensus.gtvadapter.repository.entities.EventStatus.REPLICATED;

@Slf4j
@Component
@RequiredArgsConstructor
class CustomerUpdateEventProcessor implements RepositoryEventProcessor<CustomerUpdateEvent> {

    private final CustomerEventsRepository customerEventsRepository;
    private final CustomerEventMapper customerEventMapper;
    private final GtvApiCallsRepository gtvApiCallsRepository;
    private final GtvApiCallMapper gtvApiCallMapper;

    @Override
    public String eventType() {
        return CustomerUpdateEvent.TYPE;
    }

    @Override
    public void process(CustomerUpdateEvent updateEvent) {
        String eventId = updateEvent.getEventId();

        CustomerDbEvent customerDbEvent = customerEventsRepository.findByEventId(eventId);
        if (customerDbEvent == null) {
            log.warn("Customer event with Id {} is missing from DB. Creating DB record.", eventId);
            customerDbEvent = customerEventMapper.toEventEntity(updateEvent);
        }

        customerDbEvent.setStatus(REPLICATED);
        customerEventsRepository.save(customerDbEvent);

        GtvApiCall gtvApiCall = gtvApiCallMapper.toGtvApiCall(updateEvent);
        gtvApiCallsRepository.save(gtvApiCall);
    }
}
