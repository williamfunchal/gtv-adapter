package com.consensus.gtvadapter.processor.service.account;

import com.consensus.gtvadapter.common.models.event.gtv.response.AccountCreationGtvResponse;
import com.consensus.gtvadapter.common.models.event.isp.update.CustomerUpdateEvent;
import com.consensus.gtvadapter.processor.service.SingleEventProcessor;
import org.springframework.stereotype.Component;

@Component
class AccountCreationGtvResponseProcessor implements SingleEventProcessor<AccountCreationGtvResponse> {

    @Override
    public String eventType() {
        return AccountCreationGtvResponse.TYPE;
    }

    @Override
    public CustomerUpdateEvent process(AccountCreationGtvResponse responseEvent) {
        CustomerUpdateEvent updateEvent = new CustomerUpdateEvent();
        updateEvent.setEventId(responseEvent.getEventId());
        updateEvent.setCorrelationId(responseEvent.getCorrelationId());
        updateEvent.setApi(responseEvent.getApi());
        updateEvent.setMethod(responseEvent.getMethod());
        updateEvent.setBody(responseEvent.getBody());
        updateEvent.setResult(responseEvent.getResult());
        return updateEvent;
    }
}
