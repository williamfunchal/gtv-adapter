package com.consensus.gtvadapter.processor.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class EventProcessingService {

    private final Map<String, EventProcessor> processorMappers;

    public EventProcessingService(List<EventProcessor> processorMappers) {
        this.processorMappers = processorMappers.stream()
                .collect(Collectors.toMap(
                        EventProcessor::eventType,
                        Function.identity())
                );
    }

    public AdapterEvent processEvent(AdapterEvent adapterEvent) {
        String eventType = adapterEvent.getEventType();
        @SuppressWarnings("unchecked")
        SingleEventProcessor<AdapterEvent> processorMapper = (SingleEventProcessor<AdapterEvent>) processorMappers.get(eventType);
        if (isNull(processorMapper)) {
            throw new IllegalArgumentException("Unable to process event of unknown type: " + eventType);
        }
        return processorMapper.process(adapterEvent);
    }

    public AdapterEvent processEvent(List<AdapterEvent> adapterEvents) {
        final String eventType = adapterEvents.get(0).getEventType();
        @SuppressWarnings("unchecked")
        BatchEventProcessor<AdapterEvent> processorMapper = (BatchEventProcessor<AdapterEvent>) processorMappers.get(eventType);
        if (isNull(processorMapper)) {
            throw new IllegalArgumentException("Unable to process event of unknown type: " + eventType);
        }
        return processorMapper.process(adapterEvents);
    }
}
