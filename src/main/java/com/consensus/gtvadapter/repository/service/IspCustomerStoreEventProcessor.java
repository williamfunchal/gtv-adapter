package com.consensus.gtvadapter.repository.service;

import com.consensus.gtvadapter.common.models.event.isp.store.CustomerStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.CustomerStoredEvent;
import com.consensus.gtvadapter.repository.storage.CustomerEventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class IspCustomerStoreEventProcessor implements RepositoryEventProcessor<CustomerStoreEvent> {

    private final CustomerEventsRepository customerEventsRepository;

    @Override
    public String eventType() {
        return CustomerStoreEvent.TYPE;
    }

    @Override
    public CustomerStoredEvent process(CustomerStoreEvent storeEvent) {
        CustomerStoredEvent storedEvent = new CustomerStoredEvent();
        storedEvent.setEventId(storeEvent.getEventId());
        storedEvent.setCorrelationId(storeEvent.getCorrelationId());
        storedEvent.setOperation(storeEvent.getOperation());
        storedEvent.setTableName(storeEvent.getTableName());
        storedEvent.setRawData(storeEvent.getRawData());
        storedEvent.setGtvData(storeEvent.getGtvData());
        customerEventsRepository.save(storeEvent);
        return storedEvent;
    }
}
