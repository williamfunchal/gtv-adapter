package com.consensus.gtvadapter.processor.sqs;

import com.consensus.common.sqs.*;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataStoredProcessor implements QueueMessageProcessor {

    private final ObjectMapper objectMapper;
    private final CCSIQueueListenerProperties queueProperties;
    private final GtvRequestPublishService gtvRequestPublishService;

    public DataStoredProcessor(QueueProperties queueProperties, ObjectMapper objectMapper,
            GtvRequestPublishService gtvRequestPublishService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getDataStored();
        this.gtvRequestPublishService = gtvRequestPublishService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        String correlationId = ccsiQueueMessageContext.getCorrelationId();
        String messageBody = ccsiQueueMessageContext.getMessage().getBody();
        log.info("Data-Stored event received with correlationId: {}", correlationId);

        AdapterEvent adapterEvent = parseMessage(messageBody);
        gtvRequestPublishService.publishMessage(adapterEvent);
        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    @SneakyThrows
    private AdapterEvent parseMessage(String message) {
        return objectMapper.readValue(message, AdapterEvent.class);
    }
}
