package com.consensus.gtvadapter.gateway.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.gateway.service.GtvService;
import com.consensus.gtvadapter.util.SqsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GtvRequestProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final ObjectMapper objectMapper;
    private final GtvService gtvService;
    private final GtvResponsePublishService gtvResponsePublishService;

    public GtvRequestProcessor(final QueueProperties queueProperties, final ObjectMapper objectMapper, final GtvService gtvService, final GtvResponsePublishService gtvResponsePublishService) {
        this.properties = queueProperties.getGtvDataReady();
        this.objectMapper = objectMapper;
        this.gtvService = gtvService;
        this.gtvResponsePublishService = gtvResponsePublishService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        final String messageReceived = ccsiQueueMessageContext.getMessage().getBody();
        log.info("GTV Data Ready event received with correlationId {} and body {}", correlationId, messageReceived);
        try {
            final AdapterEvent adapterEvent = parseMessage(messageReceived);
            final AdapterEvent resultEvent = gtvService.processEvent(adapterEvent);
            final String message = objectMapper.writeValueAsString(resultEvent);
            gtvResponsePublishService.publishMessageToQueue(message, SqsUtils.createMessageAttributesWithCorrelationId(correlationId));
            log.info("GTV response event published {}", message);
        }catch (JsonProcessingException jpe){
            log.error("Couldn't parse message body for event with correlationId {} Cause: {}", correlationId, jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.NON_RECOVERABLE_ERROR)
                    .build();
        }catch (Exception e){
            log.error("Couldn't process message with correlationId {} Cause: {}", correlationId, e.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
                    .build();
        }


        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    private AdapterEvent parseMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, AdapterEvent.class);
    }
}
