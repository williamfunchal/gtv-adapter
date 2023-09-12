package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.gtv.request.AccountCreationGtvRequest;
import com.consensus.gtvadapter.common.models.event.isp.ready.IspCustomerNewEvent;
import com.consensus.gtvadapter.common.models.event.isp.stored.IspCustomerStoredEvent;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
class IspCustomerStoredEventProcessor implements EventProcessor<IspCustomerStoredEvent> {

    private static final String ACCOUNT_CREATE_API = "/billing/2/billing-accounts";

    @Override
    public String eventType() {
        return IspCustomerStoredEvent.TYPE;
    }

    @Override
    public AccountCreationGtvRequest process(IspCustomerStoredEvent event) {
        AccountCreationGtvRequest gtvRequest = new AccountCreationGtvRequest();
        gtvRequest.setEventId(event.getEventId());
        gtvRequest.setCorrelationId(event.getCorrelationId());
        gtvRequest.setApi(ACCOUNT_CREATE_API);
        gtvRequest.setMethod(HttpMethod.POST);
        gtvRequest.setBody(event.getGtvData());
        return gtvRequest;
    }
}
