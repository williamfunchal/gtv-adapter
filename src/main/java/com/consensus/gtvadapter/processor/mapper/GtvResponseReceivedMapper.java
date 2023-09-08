package com.consensus.gtvadapter.processor.mapper;

import org.springframework.stereotype.Component;

import com.consensus.gtvadapter.common.models.event.AdapterDataReadyToUpdateEvent;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.GtvResponseReceivedEvent;

@Component
public class GtvResponseReceivedMapper {
    //Map from GtvResponseReceivedEvent to AdapterDataReadyToUpdateEvent
    public AdapterDataReadyToUpdateEvent map(GtvResponseReceivedEvent message) {
        AdapterDataReadyToUpdateEvent adapterDataReadyToUpdateEvent = new AdapterDataReadyToUpdateEvent();
        adapterDataReadyToUpdateEvent.setEventType(AdapterDataReadyToUpdateEvent.TYPE);
        adapterDataReadyToUpdateEvent.setRequest(message.getRequest());
        adapterDataReadyToUpdateEvent.setResult(message.getResult());
        return adapterDataReadyToUpdateEvent;
    }
}
