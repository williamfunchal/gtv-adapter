package com.consensus.gtvadapter.processor.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.DataMappingStoreEvent;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.mapper.ProcessorMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.DateTimeException;

@Slf4j
public abstract class BaseDataReadyProcessor implements QueueMessageProcessor {

    protected final ObjectMapper objectMapper;
    protected final CCSIQueueListenerProperties queueProperties;
    protected final StoreDataPublishService storeDataPublishService;
    protected final ProcessorMapperService processorMapperService;

    public BaseDataReadyProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            StoreDataPublishService storeDataPublishService, ProcessorMapperService processorMapperService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getIspDataReady();
        this.storeDataPublishService = storeDataPublishService;
        this.processorMapperService = processorMapperService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        String correlationId = ccsiQueueMessageContext.getCorrelationId();
        String messageBody = ccsiQueueMessageContext.getMessage().getBody();
        log.debug("ISP-Data change event received with correlationId: {} and message body {}", correlationId, messageBody);

        AdapterEvent adapterEvent;
        try {
            adapterEvent = parseMessageBody(messageBody);
        } catch (JsonProcessingException jpe) {
            log.error("Unable to parse message body: {}", jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("Message body parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        }

        AdapterEvent nextEvent;
        try {
            nextEvent = processorMapperService.mapGtvRequest(adapterEvent);
        } catch (DateTimeException dte) {
            log.error("Failed when trying to parse date: {}", dte.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("Date parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        } catch (Exception e) {
            log.error("GTV request mapping failed: {}", e.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("GTV request mapping failed")
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
                    .build();
        }

        if (nextEvent.getEventType().equals(DataMappingStoreEvent.TYPE)) {
            storeDataPublishService.publishMessage(nextEvent);
            log.info("Data Mapping Store Event published {}", nextEvent);
        }

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    private AdapterEvent parseMessageBody(String messageBody) throws JsonProcessingException {
        return objectMapper.readValue(messageBody, AdapterEvent.class);
    }
}
