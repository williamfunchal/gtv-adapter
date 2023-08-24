package com.consensus.gtvadapter.processor.sqs;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;

import java.time.DateTimeException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.common.models.MappedData;
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
        log.info("ISP-Data change event received with correlationId: {}", correlationId);

        final IspRawData ispRawData;
        try{
            ispRawData = parseMessageBody(ccsiQueueMessageContext.getMessage().getBody());
            log.debug("ISP-Data event payload: {}", ispRawData);
        }catch (JsonProcessingException jpe){
            log.error("Unable to parse message body: {}", jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("Message body parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        }

        final MappedData mappedData = new MappedData();
        try {
            final List<GtvRequest> gtvRequests = processorMapper.mapGtvRequest(ispRawData);
            mappedData.setRequests(gtvRequests);
        }catch (DateTimeException dte){
            log.error("Failed when trying to parse date: {}", dte.getMessage());
            return CCSIQueueMessageResult.builder()
                    .logMessage("Date parsing failed")
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        }

        final IspGtvMapping ispGtvMapping = new IspGtvMapping();
        ispGtvMapping.setRawData(ispRawData);
        ispGtvMapping.setMappedData(mappedData);
        ispGtvMapping.setCorrelationId(correlationId);

        final String message = createMessage(ispGtvMapping, UUID.fromString(ispRawData.getCorrelationId()));
        storeDataPublishService.publishMessageToQueue(message, getMessageAttributes(correlationId));

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    private Map<String, MessageAttributeValue> getMessageAttributes(String correlationId){
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        final MessageAttributeValue correlationIdAttribute = SqsUtils.createAttribute(correlationId);
        attributes.put(CORRELATION_ID, correlationIdAttribute);
        return attributes;
    }

    private IspRawData parseMessageBody(String messageBody) throws JsonProcessingException {
        return objectMapper.readValue(messageBody, IspRawData.class);
    }

    @SneakyThrows
    private String createMessage(IspGtvMapping ispGtvMapping, UUID correlationId){
        final DataMappingStoreEvent dataMappingStoreEvent = new DataMappingStoreEvent();
        dataMappingStoreEvent.setCorrelationId(correlationId);
        dataMappingStoreEvent.setEventType(DataMappingStoreEvent.TYPE);
        dataMappingStoreEvent.setIspGtvMapping(ispGtvMapping);
        return objectMapper.writeValueAsString(dataMappingStoreEvent);
    }
}
