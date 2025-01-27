package com.consensus.gtvadapter.processor.sqs;

import com.consensus.common.sqs.*;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.service.EventProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataStoredProcessor implements QueueMessageProcessor {

    private final ObjectMapper objectMapper;
    private final CCSIQueueListenerProperties queueProperties;
    private final EventProcessingService eventProcessingService;
    private final GtvDataRequestPublishService gtvDataRequestPublishService;

    public DataStoredProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            GtvDataRequestPublishService gtvDataRequestPublishService, EventProcessingService eventProcessingService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getDataStored();
        this.eventProcessingService = eventProcessingService;
        this.gtvDataRequestPublishService = gtvDataRequestPublishService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext messageContext) {
        log.debug("Processing SQS message, Type: {}, Body: {}", messageContext.getEventType(), messageContext.getMessage().getBody());

        AdapterEvent adapterEvent = parseMessage(messageContext.getMessage().getBody());
        AdapterEvent nextEvent = eventProcessingService.processEvent(adapterEvent);
        gtvDataRequestPublishService.publishMessage(nextEvent);

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    @SneakyThrows
    private AdapterEvent parseMessage(String message) {
        return objectMapper.readValue(message, AdapterEvent.class);
    }
}
