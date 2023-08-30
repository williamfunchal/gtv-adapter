package com.consensus.gtvadapter.gateway.service.processor;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;

public interface EventProcessor<T extends AdapterEvent> {

    String eventType();

    AdapterEvent process(T message);
}
