package com.consensus.gtvadapter.gateway.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.ResultsEvent;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.gateway.service.GtvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GtvRequestProcessor implements QueueMessageProcessor {

    private final ObjectMapper objectMapper;
    private final CCSIQueueListenerProperties queueProperties;
    private final GtvResponsePublishService gtvResponsePublishService;
    private final GtvService gtvService;

    public GtvRequestProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            GtvResponsePublishService gtvResponsePublishService, GtvService gtvService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getGtvRequest();
        this.gtvResponsePublishService = gtvResponsePublishService;
        this.gtvService = gtvService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        final String messageReceived = ccsiQueueMessageContext.getMessage().getBody();
        log.info("GTV Data Ready event received with correlationId {} and body {}", correlationId, messageReceived);
        try {
            AdapterEvent adapterEvent = parseMessage(messageReceived);
            ResultsEvent resultsEvent = (ResultsEvent) gtvService.processEvent(adapterEvent);
            gtvResponsePublishService.publishMessage(resultsEvent);
            log.info("GTV response event published {}", resultsEvent);
            if (resultsEvent.getResult().getStatusCode() > 499) {
                return CCSIQueueMessageResult.builder()
                        .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
                        .build();
            }
        } catch (JsonProcessingException jpe) {
            log.error("Couldn't parse message body for event with correlationId {} Cause: {}", correlationId, jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.NON_RECOVERABLE_ERROR)
                    .build();
        } catch (Exception e) {
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
