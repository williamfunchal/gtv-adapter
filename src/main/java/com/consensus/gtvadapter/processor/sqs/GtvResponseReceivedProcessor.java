package com.consensus.gtvadapter.processor.sqs;

import org.springframework.stereotype.Component;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterDataReadyToUpdateEvent;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.GtvResponseReceivedEvent;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.mapper.GtvResponseReceivedMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GtvResponseReceivedProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final AdapterDataReadyToUpdatePublishService adapterDataReadyToUpdatePublishService;
    private final ObjectMapper objectMapper;
    private final GtvResponseReceivedMapper gtvResponseReceivedMapper;

    public GtvResponseReceivedProcessor(final QueueProperties queueProperties,
            final AdapterDataReadyToUpdatePublishService adapterDataReadyToUpdatePublishService,
            final ObjectMapper objectMapper,
            final GtvResponseReceivedMapper gtvResponseReceivedMapper) {
        this.properties = queueProperties.getGtvResponseReceived();
        this.adapterDataReadyToUpdatePublishService = adapterDataReadyToUpdatePublishService;
        this.objectMapper = objectMapper;
        this.gtvResponseReceivedMapper = gtvResponseReceivedMapper;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        String correlationId = ccsiQueueMessageContext.getCorrelationId();
        String messageBody = ccsiQueueMessageContext.getMessage().getBody();
        log.info("gtv-response-received event received with correlationId: {}", correlationId);


        try{
            GtvResponseReceivedEvent gtvResponseReceivedEvent = (GtvResponseReceivedEvent )parseMessageBody(messageBody);
            log.info("gtv-response-received event received: {}", gtvResponseReceivedEvent);
            AdapterDataReadyToUpdateEvent adapterDataReadyToUpdateEvent = gtvResponseReceivedMapper.map(gtvResponseReceivedEvent);
            adapterDataReadyToUpdatePublishService.publishMessage(adapterDataReadyToUpdateEvent);
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.SUCCESS)
                    .build();
        } catch (JsonProcessingException jpe) {
            log.error("Unable to parse message body: {}", jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("Message body parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        }
    }

    private AdapterEvent parseMessageBody(String messageBody) throws JsonProcessingException {
        return objectMapper.readValue(messageBody, AdapterEvent.class);
    }

    @SneakyThrows
    private String createMessage(AdapterEvent adapterEvent) {
        return objectMapper.writeValueAsString(adapterEvent);
    }
}
