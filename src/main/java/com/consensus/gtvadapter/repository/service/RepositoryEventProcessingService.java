package com.consensus.gtvadapter.repository.service;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class RepositoryEventProcessingService {

    private final Map<String, RepositoryEventProcessor<? extends AdapterEvent>> repositoryProcessors;

    public RepositoryEventProcessingService(List<RepositoryEventProcessor<? extends AdapterEvent>> repositoryProcessors) {
        this.repositoryProcessors = repositoryProcessors.stream()
                .collect(Collectors.toMap(
                        RepositoryEventProcessor::eventType,
                        Function.identity())
                );
    }

    public void process(AdapterEvent adapterEvent) {
        String eventType = adapterEvent.getEventType();
        @SuppressWarnings("unchecked")
        RepositoryEventProcessor<AdapterEvent> processorMapper = (RepositoryEventProcessor<AdapterEvent>) repositoryProcessors.get(eventType);
        if (isNull(processorMapper)) {
            throw new IllegalArgumentException("Unable to process event of unknown type: " + eventType);
        }
        processorMapper.process(adapterEvent);
    }

}
