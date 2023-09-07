package com.consensus.gtvadapter.gateway.service.processor;

import com.consensus.gtvadapter.common.models.event.AccountCreationResultsEvent;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.GtvAccountCreationEvent;
import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import com.consensus.gtvadapter.gateway.client.GtvRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GtvAccountCreationEventProcessor implements EventProcessor<GtvAccountCreationEvent> {

    private final GtvRestClient gtvRestClient;

    @Override
    public String eventType() {
        return GtvAccountCreationEvent.TYPE;
    }

    @Override
    public AdapterEvent process(GtvAccountCreationEvent event) {
        GtvRequestDetails requestDetails = gtvRestClient.createAccount(event);
        AccountCreationResultsEvent resultEvent = new AccountCreationResultsEvent();
        resultEvent.setEventId(event.getEventId());
        resultEvent.setCorrelationId(event.getCorrelationId());
        resultEvent.setApi(event.getApi());
        resultEvent.setMethod(event.getMethod());
        resultEvent.setBody(event.getBody());
        resultEvent.setResult(requestDetails);
        return resultEvent;
    }
}
