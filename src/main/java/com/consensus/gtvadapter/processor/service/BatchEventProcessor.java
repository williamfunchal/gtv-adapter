package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;

import java.util.List;

public interface BatchEventProcessor<T extends AdapterEvent> extends EventProcessor {

    AdapterEvent process(List<T> messages);

}
