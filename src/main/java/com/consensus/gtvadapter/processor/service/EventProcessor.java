package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;

interface EventProcessor<T extends AdapterEvent> {

    String eventType();

    AdapterEvent process(T message);
}
