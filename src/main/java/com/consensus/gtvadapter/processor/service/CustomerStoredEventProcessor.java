package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.gtv.request.AccountCreationGtvRequest;
import com.consensus.gtvadapter.common.models.event.isp.stored.CustomerStoredEvent;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
class CustomerStoredEventProcessor implements SingleEventProcessor<CustomerStoredEvent> {

    private static final String ACCOUNT_CREATE_API = "/billing/2/billing-accounts";

    @Override
    public String eventType() {
        return CustomerStoredEvent.TYPE;
    }

    @Override
    public AccountCreationGtvRequest process(CustomerStoredEvent event) {
        AccountCreationGtvRequest gtvRequest = new AccountCreationGtvRequest();
        gtvRequest.setEventId(event.getEventId());
        gtvRequest.setCorrelationId(event.getCorrelationId());
        gtvRequest.setApi(ACCOUNT_CREATE_API);
        gtvRequest.setMethod(HttpMethod.POST);
        gtvRequest.setBody(event.getGtvData());
        return gtvRequest;
    }
}
