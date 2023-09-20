package com.consensus.gtvadapter.gateway.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.gtv.request.BaseGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.response.BaseGtvResponse;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.gateway.service.GatewayEventProcessingService;
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
    private final GatewayEventProcessingService gatewayEventProcessingService;

    public GtvRequestProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            GtvResponsePublishService gtvResponsePublishService, GatewayEventProcessingService gatewayEventProcessingService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getGtvRequest();
        this.gtvResponsePublishService = gtvResponsePublishService;
        this.gatewayEventProcessingService = gatewayEventProcessingService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext messageContext) {
        log.debug("Processing SQS message, Type: {}, Body: {}", messageContext.getEventType(), messageContext.getMessage().getBody());
        try {
            BaseGtvRequest<?> request = parseMessage(messageContext.getMessage().getBody());
            BaseGtvResponse<?> response = gatewayEventProcessingService.processEvent(request);
            gtvResponsePublishService.publishMessage(response);
            if (response.getResult().getStatusCode() > 499) {
                return CCSIQueueMessageResult.builder()
                        .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
                        .build();
            }
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

    private BaseGtvRequest<?> parseMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, BaseGtvRequest.class);
    }
}
