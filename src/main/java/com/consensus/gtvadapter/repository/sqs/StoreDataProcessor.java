package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.config.QueueProperties;
import com.consensus.gtvadapter.repository.service.RepositoryService;
import com.consensus.gtvadapter.util.SqsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;

@Slf4j
@Component
public class StoreDataProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final DataStoredPublishService dataStoredPublishService;
    private final ObjectMapper objectMapper;
    private final RepositoryService repositoryService;

    public StoreDataProcessor(final QueueProperties queueProperties, final DataStoredPublishService dataStoredPublishService, final ObjectMapper objectMapper, final RepositoryService repositoryService) {
        this.properties = queueProperties.getStoreData();
        this.dataStoredPublishService = dataStoredPublishService;
        this.objectMapper = objectMapper;
        this.repositoryService = repositoryService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        log.info("Add-Data event received with correlationId: {}", correlationId);
        try {
            final AdapterEvent adapterEvent = parseMessage(ccsiQueueMessageContext.getMessage().getBody());
            repositoryService.saveIspGtvMapping(adapterEvent);
            dataStoredPublishService.publishMessageToQueue(ccsiQueueMessageContext.getMessage().getBody(), getMessageAttributes(correlationId));
        }catch (JsonProcessingException jpe){
            log.error("Couldn't parse message body for event with correlationId {} Cause: {}", correlationId, jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.NON_RECOVERABLE_ERROR)
                    .build();
        }catch (AmazonDynamoDBException ddbe){
            log.error("Couldn't save data mapping with correlationId {} Cause: {}", correlationId, ddbe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
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

    private Map<String, MessageAttributeValue> getMessageAttributes(String correlationId){
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        final MessageAttributeValue correlationIdAttribute = SqsUtils.createAttribute(correlationId);
        attributes.put(CORRELATION_ID, correlationIdAttribute);
        return attributes;
    }

    private AdapterEvent parseMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, AdapterEvent.class);
    }
}