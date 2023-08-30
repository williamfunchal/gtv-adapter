package com.consensus.gtvadapter.processor.mapper;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;

interface ProcessorMapper<T extends AdapterEvent> {

    String eventType();

    AdapterEvent process(T message);
}
