package com.consensus.gtvadapter.processor.mapper;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class ProcessorMapperService {

    private final Map<String, ProcessorMapper<? extends AdapterEvent>> processorMappers;

    public ProcessorMapperService(List<ProcessorMapper<? extends AdapterEvent>> processorMappers) {
        this.processorMappers = processorMappers.stream()
                .collect(Collectors.toMap(
                        ProcessorMapper::eventType,
                        Function.identity(),
                        (o1, o2) -> o1)
                );
    }

    public AdapterEvent mapGtvRequest(AdapterEvent adapterEvent){
        String eventType = adapterEvent.getEventType();
        @SuppressWarnings("unchecked")
        ProcessorMapper<AdapterEvent> processorMapper = (ProcessorMapper<AdapterEvent>) processorMappers.get(eventType);
        if (isNull(processorMapper)) {
            throw new IllegalArgumentException("Unable to process event of unknown type: " + eventType);
        }
        return processorMapper.process(adapterEvent);
    }
}
