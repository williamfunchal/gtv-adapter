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
        AccountCreationResultsEvent accountCreationResultsEvent = new AccountCreationResultsEvent();
        accountCreationResultsEvent.setApi(event.getApi());
        accountCreationResultsEvent.setMethod(event.getMethod());
        accountCreationResultsEvent.setBody(event.getBody());
        accountCreationResultsEvent.setResult(requestDetails);
        accountCreationResultsEvent.setCorrelationId(event.getCorrelationId());
        return accountCreationResultsEvent;
    }
}
