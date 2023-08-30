package com.consensus.gtvadapter.gateway.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.gateway.service.processor.EventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class GtvService {

    private final Map<String, EventProcessor<? extends AdapterEvent>> eventProcessors;

    public GtvService(List<EventProcessor<? extends AdapterEvent>> eventProcessors) {
        this.eventProcessors = eventProcessors.stream()
                .collect(Collectors.toMap(
                        EventProcessor::eventType,
                        Function.identity(),
                        (o1, o2) -> o1)
                );
    }

    public AdapterEvent processEvent(AdapterEvent adapterEvent){
        String eventType = adapterEvent.getEventType();
        @SuppressWarnings("unchecked")
        EventProcessor<AdapterEvent> eventProcessor = (EventProcessor<AdapterEvent>) eventProcessors.get(eventType);
        if (isNull(eventProcessor)) {
            throw new IllegalArgumentException("Unable to process SQS event of unknown type: " + eventType);
        }
        return eventProcessor.process(adapterEvent);
    }
}
