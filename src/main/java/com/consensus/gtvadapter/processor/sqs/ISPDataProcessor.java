package com.consensus.gtvadapter.processor.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.common.models.MappedData;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.DataMappingStoreEvent;
import com.consensus.gtvadapter.common.models.rawdata.IspRawData;
import com.consensus.gtvadapter.common.models.request.GtvRequest;
import com.consensus.gtvadapter.config.QueueProperties;
import com.consensus.gtvadapter.processor.mapper.ProcessorMapper;
import com.consensus.gtvadapter.util.SqsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ISPDataProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final StoreDataPublishService storeDataPublishService;
    private final ObjectMapper objectMapper;
    private final ProcessorMapper processorMapper;

    public ISPDataProcessor(final QueueProperties queueProperties, final StoreDataPublishService storeDataPublishService, final ObjectMapper objectMapper, final ProcessorMapper processorMapper) {
        this.properties = queueProperties.getIspDataReady();
        this.storeDataPublishService = storeDataPublishService;
        this.objectMapper = objectMapper;
        this.processorMapper = processorMapper;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        final String messageBody = ccsiQueueMessageContext.getMessage().getBody();
        log.info("ISP-Data change event received with correlationId: {} and message body {}", correlationId, messageBody);

        final AdapterEvent adapterEvent;

        try{
            adapterEvent = parseMessageBody(messageBody);
        }catch (JsonProcessingException jpe){
            log.error("Unable to parse message body: {}", jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("Message body parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        }

        AdapterEvent nextEvent;
        try {
            nextEvent = processorMapper.mapGtvRequest(adapterEvent);
        }catch (DateTimeException dte){
            log.error("Failed when trying to parse date: {}", dte.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("Date parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        }

        final String message = createMessage(nextEvent);

        if(nextEvent.getEventType().equals(DataMappingStoreEvent.TYPE)){
            storeDataPublishService.publishMessageToQueue(message, SqsUtils.createMessageAttributesWithCorrelationId(correlationId));
            log.info("Data Mapping Store Event published {}", message);
        }

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    private AdapterEvent parseMessageBody(String messageBody) throws JsonProcessingException {
        return objectMapper.readValue(messageBody, AdapterEvent.class);
    }

    @SneakyThrows
    private String createMessage(AdapterEvent adapterEvent){
        return objectMapper.writeValueAsString(adapterEvent);
    }
}
