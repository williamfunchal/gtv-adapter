package com.consensus.gtvadapter.gateway.service;

import com.consensus.gtvadapter.common.models.event.AccountCreationResultsEvent;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.GtvAccountCreationEvent;
import com.consensus.gtvadapter.gateway.client.GtvRestClient;
import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GtvService {

    private final GtvRestClient gtvRestClient;

    public AdapterEvent processEvent(AdapterEvent adapterEvent){

        if(GtvAccountCreationEvent.TYPE.equals(adapterEvent.getEventType())){
            final GtvAccountCreationEvent accountCreationEvent = (GtvAccountCreationEvent) adapterEvent;
            final GtvRequestDetails requestDetails = gtvRestClient.createAccount(accountCreationEvent);
            final AccountCreationResultsEvent accountCreationResultsEvent = new AccountCreationResultsEvent();
            accountCreationResultsEvent.setApi(accountCreationEvent.getApi());
            accountCreationResultsEvent.setMethod(accountCreationEvent.getMethod());
            accountCreationResultsEvent.setBody(accountCreationEvent.getBody());
            accountCreationResultsEvent.setResult(requestDetails);
            accountCreationResultsEvent.setCorrelationId(accountCreationEvent.getCorrelationId());
            return accountCreationResultsEvent;
        }
        return null;
    }
}
