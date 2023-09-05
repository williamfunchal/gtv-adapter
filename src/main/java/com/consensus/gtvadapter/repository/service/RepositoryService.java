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
public class RepositoryService {

    private final Map<String, RepositoryProcessor<? extends AdapterEvent>> repositoryProcessors;

    public RepositoryService(List<RepositoryProcessor<? extends AdapterEvent>> repositoryProcessors) {
        this.repositoryProcessors = repositoryProcessors.stream()
                .collect(Collectors.toMap(
                        RepositoryProcessor::eventType,
                        Function.identity())
                );
    }

    public AdapterEvent process(AdapterEvent adapterEvent) {
        String eventType = adapterEvent.getEventType();
        @SuppressWarnings("unchecked")
        RepositoryProcessor<AdapterEvent> processorMapper = (RepositoryProcessor<AdapterEvent>) repositoryProcessors.get(eventType);
        if (isNull(processorMapper)) {
            throw new IllegalArgumentException("Unable to process event of unknown type: " + eventType);
        }
        return processorMapper.process(adapterEvent);
    }

}
