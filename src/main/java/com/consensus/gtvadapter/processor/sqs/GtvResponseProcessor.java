package com.consensus.gtvadapter.processor.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.gtv.response.BaseGtvResponse;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GtvResponseProcessor implements QueueMessageProcessor {

    private final ObjectMapper objectMapper;
    private final CCSIQueueListenerProperties queueProperties;

    public GtvResponseProcessor(ObjectMapper objectMapper, QueueProperties queueProperties) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getGtvResponse();
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext messageContext) {
        log.debug("Processing SQS message of type: {}", messageContext.getEventType());
        try {
            BaseGtvResponse<?> request = parseMessage(messageContext.getMessage().getBody());
            //todo Implement processing of GTV response events from Gateway API

        } catch (JsonProcessingException jpEx) {
            log.error("Exception parsing SQS event: {}", jpEx.getMessage(), jpEx);
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.NON_RECOVERABLE_ERROR)
                    .build();
        } catch (Exception ex) {
            log.error("Exception processing SQS event: {}", ex.getMessage(), ex);
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
                    .build();
        }

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    private BaseGtvResponse<?> parseMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, BaseGtvResponse.class);
    }
}
