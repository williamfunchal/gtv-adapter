package com.consensus.gtvadapter.repository.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;

interface RepositoryProcessor<T extends AdapterEvent> {

    String eventType();

    AdapterEvent process(T message);
}
