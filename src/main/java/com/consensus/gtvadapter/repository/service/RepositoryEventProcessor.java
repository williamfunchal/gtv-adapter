package com.consensus.gtvadapter.repository.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;

interface RepositoryEventProcessor<T extends AdapterEvent> {

    String eventType();

    void process(T message);
}
