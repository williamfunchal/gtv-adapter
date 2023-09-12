package com.consensus.gtvadapter.processor.sqs;

import com.consensus.common.sqs.*;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.isp.ready.BaseIspDataReadyEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.BaseIspDataStoreEvent;
import com.consensus.gtvadapter.common.models.event.isp.update.BaseIspDataUpdateEvent;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.service.EventProcessingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.DateTimeException;
import java.util.Optional;

import static com.consensus.gtvadapter.util.GtvConstants.SqsMessageAttributes.CORRELATION_ID;
import static com.consensus.gtvadapter.util.GtvConstants.SqsMessageAttributes.EVENT_ID;

@Slf4j
public abstract class BaseDataReadyProcessor implements QueueMessageProcessor {

    protected final ObjectMapper objectMapper;
    protected final CCSIQueueListenerProperties queueProperties;
    protected final DataReadyToStorePublishService dataReadyToStorePublishService;
    protected final DataReadyToUpdatePublishService dataReadyToUpdatePublishService;
    protected final EventProcessingService eventProcessingService;

    public BaseDataReadyProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            DataReadyToStorePublishService dataReadyToStorePublishService, DataReadyToUpdatePublishService dataReadyToUpdatePublishService,
            EventProcessingService eventProcessingService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getIspDataReady();
        this.dataReadyToStorePublishService = dataReadyToStorePublishService;
        this.dataReadyToUpdatePublishService = dataReadyToUpdatePublishService;
        this.eventProcessingService = eventProcessingService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext messageContext) {
        log.debug("Processing SQS message of type: {}", messageContext.getEventType());

        BaseIspDataReadyEvent<?> dataReadyEvent;
        try {
            dataReadyEvent = parseSqsEvent(messageContext.getMessage());
        } catch (JsonProcessingException jpEx) {
            log.error("Exception parsing SQS event: {}", jpEx.getMessage(), jpEx);
            return CCSIQueueMessageResult.builder()
                    .logMessage("Message body parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        }

        AdapterEvent processedEvent;
        try {
            processedEvent = eventProcessingService.processEvent(dataReadyEvent);
        } catch (DateTimeException dtEx) {
            log.error("Exception parsing date from SQS event: {}", dtEx.getMessage(), dtEx);
            return CCSIQueueMessageResult.builder()
                    .logMessage("Date parsing failed: " + dtEx.getMessage())
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        } catch (Exception ex) {
            log.error("Exception processing SQS event: {}", ex.getMessage(), ex);
            return CCSIQueueMessageResult.builder()
                    .logMessage("GTV request mapping failed")
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
                    .build();
        }

        if (processedEvent instanceof BaseIspDataStoreEvent) {
            dataReadyToStorePublishService.publishMessage(processedEvent);
            log.debug("Data Mapping Store Event published: {}", processedEvent);
        } else if (processedEvent instanceof BaseIspDataUpdateEvent) {
            dataReadyToUpdatePublishService.publishMessage(dataReadyEvent);
            log.debug("Data Mapping Update Event published: {}", processedEvent);
        }

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    protected BaseIspDataReadyEvent<?> parseSqsEvent(CCSIQueueMessage queueMessage) throws JsonProcessingException {
        var ispReadyEvent = objectMapper.readValue(queueMessage.getBody(), BaseIspDataReadyEvent.class);

        // Need to extract 'eventId' and 'correlationId' from message attributes as Poller will not send them within payload
        String eventId = Optional.ofNullable(queueMessage.getMessageAttributes())
                .map(attrs -> attrs.get(EVENT_ID))
                .orElse(ispReadyEvent.getEventId());
        ispReadyEvent.setEventId(eventId);

        String correlationId = Optional.ofNullable(queueMessage.getMessageAttributes())
                .map(attrs -> attrs.get(CORRELATION_ID))
                .orElse(ispReadyEvent.getCorrelationId());
        ispReadyEvent.setCorrelationId(correlationId);

        return ispReadyEvent;
    }
}
