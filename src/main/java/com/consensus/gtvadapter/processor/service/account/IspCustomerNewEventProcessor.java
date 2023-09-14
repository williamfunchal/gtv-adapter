package com.consensus.gtvadapter.processor.service.account;

import com.consensus.gtvadapter.common.models.event.isp.ready.IspCustomerNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.CustomerStoreEvent;
import com.consensus.gtvadapter.processor.service.SingleEventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class IspCustomerNewEventProcessor implements SingleEventProcessor<IspCustomerNewEvent> {

    private final AccountMapper accountMapper;

    @Override
    public String eventType() {
        return IspCustomerNewEvent.TYPE;
    }

    @Override
    public CustomerStoreEvent process(IspCustomerNewEvent event) {
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
