package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.repository.service.RepositoryEventProcessingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataReadyToStoreProcessor implements QueueMessageProcessor {

    private final ObjectMapper objectMapper;
    private final CCSIQueueListenerProperties queueProperties;
    private final DataStoredPublishService dataStoredPublishService;
    private final RepositoryEventProcessingService repositoryEventProcessingService;

    public DataReadyToStoreProcessor(QueueProperties queueProperties, ObjectMapper objectMapper,
            DataStoredPublishService dataStoredPublishService, RepositoryEventProcessingService repositoryEventProcessingService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getDataReadyToStore();
        this.dataStoredPublishService = dataStoredPublishService;
        this.repositoryEventProcessingService = repositoryEventProcessingService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext messageContext) {
        log.debug("Processing SQS message of type: {}", messageContext.getEventType());
        try {
            AdapterEvent adapterEvent = parseMessage(messageContext.getMessage().getBody());
            AdapterEvent storedEvent = repositoryEventProcessingService.process(adapterEvent);
            dataStoredPublishService.publishMessage(storedEvent);
        } catch (JsonProcessingException jpEx) {
            log.error("Exception parsing SQS event: {}", jpEx.getMessage(), jpEx);
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.NON_RECOVERABLE_ERROR)
                    .build();
        } catch (AmazonDynamoDBException ddbEx) {
            log.error("Exception storing data to DynamoDb: {}", ddbEx.getMessage(), ddbEx);
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
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

    private AdapterEvent parseMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, AdapterEvent.class);
    }
}
