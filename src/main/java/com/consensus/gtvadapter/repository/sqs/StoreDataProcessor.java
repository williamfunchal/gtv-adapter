package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.repository.service.RepositoryService;
import com.consensus.gtvadapter.util.SqsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StoreDataProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final DataStoredPublishService dataStoredPublishService;
    private final ObjectMapper objectMapper;
    private final RepositoryService repositoryService;

    public StoreDataProcessor(QueueProperties queueProperties, DataStoredPublishService dataStoredPublishService,
            ObjectMapper objectMapper, RepositoryService repositoryService) {
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
        String correlationId = ccsiQueueMessageContext.getCorrelationId();
        String messageBody = ccsiQueueMessageContext.getMessage().getBody();
        log.info("Store data event received with correlationId {} and body {}", correlationId, messageBody);
        try {

            AdapterEvent adapterEvent = parseMessage(messageBody);
            AdapterEvent storedEvent = repositoryService.process(adapterEvent);
            dataStoredPublishService.publishMessageToQueue(objectMapper.writeValueAsString(storedEvent),
                    SqsUtils.createMessageAttributesWithCorrelationId(correlationId),
                    "repository"
            );
            log.info("Data stored event published {}", messageBody);
        } catch (JsonProcessingException jpe) {
            log.error("Couldn't parse message body for event with correlationId {} Cause: {}", correlationId, jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.NON_RECOVERABLE_ERROR)
                    .build();
        } catch (AmazonDynamoDBException ddbe) {
            log.error("Couldn't save data mapping with correlationId {} Cause: {}", correlationId, ddbe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
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
