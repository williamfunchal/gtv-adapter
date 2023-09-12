package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.isp.ready.IspCustomerNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.IspCustomerStoreEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class IspCustomerNewEventProcessor implements EventProcessor<IspCustomerNewEvent> {

    private final AccountMapper accountMapper;

    @Override
    public String eventType() {
        return IspCustomerNewEvent.TYPE;
    }

    @Override
    public IspCustomerStoreEvent process(IspCustomerNewEvent event) {
        IspCustomerStoreEvent storeEvent = new IspCustomerStoreEvent();
        storeEvent.setEventId(event.getEventId());
        storeEvent.setCorrelationId(event.getCorrelationId());
        storeEvent.setOperation(event.getOperation());
        storeEvent.setTableName(event.getTableName());
        storeEvent.setRawData(event.getData());
        storeEvent.setGtvData(accountMapper.toAccountCreationData(storeEvent.getRawData()));
        return storeEvent;
    }
}
