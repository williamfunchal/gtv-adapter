package com.consensus.gtvadapter.gateway.service;

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

    public GtvRequestDetails processEvent(AdapterEvent adapterEvent){

        GtvRequestDetails requestDetails = null;

        if(GtvAccountCreationEvent.TYPE.equals(adapterEvent.getEventType())){
            final GtvAccountCreationEvent accountCreationEvent = (GtvAccountCreationEvent) adapterEvent;
            requestDetails = gtvRestClient.createAccount(accountCreationEvent);
        }
        return requestDetails;
    }
}
