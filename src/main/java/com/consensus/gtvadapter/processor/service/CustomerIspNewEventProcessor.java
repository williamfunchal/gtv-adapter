package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.isp.ready.CustomerIspNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.CustomerStoreEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CustomerIspNewEventProcessor implements EventProcessor<CustomerIspNewEvent> {

    private final AccountMapper accountMapper;

    @Override
    public String eventType() {
        return CustomerIspNewEvent.TYPE;
    }

    @Override
    public CustomerStoreEvent process(CustomerIspNewEvent event) {
        CustomerStoreEvent storeEvent = new CustomerStoreEvent();
        storeEvent.setEventId(event.getEventId());
        storeEvent.setCorrelationId(event.getCorrelationId());
        storeEvent.setOperation(event.getOperation());
        storeEvent.setTableName(event.getTableName());
        storeEvent.setRawData(event.getData());
        storeEvent.setGtvData(accountMapper.toAccountCreationData(storeEvent.getRawData()));
        return storeEvent;
    }
}
