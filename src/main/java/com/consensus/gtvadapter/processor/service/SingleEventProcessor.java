package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;

public interface SingleEventProcessor<T extends AdapterEvent> extends EventProcessor {

    AdapterEvent process(T message);
}
