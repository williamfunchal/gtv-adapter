package com.consensus.gtvadapter.processor.sqs;

import org.springframework.stereotype.Component;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.DataMappingStoredEvent;
import com.consensus.gtvadapter.common.models.event.GtvAccountCreationEvent;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.mapper.GtvAccountReadyMapper;
import com.consensus.gtvadapter.repository.service.RepositoryService;
import com.consensus.gtvadapter.util.SqsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataStoredProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final GtvDataReadyPublishService gtvDataReadyPublishService;
    private final ObjectMapper objectMapper;
    private final GtvAccountReadyMapper gtvAccountReadyMapper;

    public DataStoredProcessor(final QueueProperties queueProperties, final GtvDataReadyPublishService gtvDataReadyPublishService, final ObjectMapper objectMapper, final RepositoryService repositoryService, final GtvAccountReadyMapper gtvAccountReadyMapper) {
        this.properties = queueProperties.getDataStored();
        this.gtvDataReadyPublishService = gtvDataReadyPublishService;
        this.objectMapper = objectMapper;
        this.gtvAccountReadyMapper = gtvAccountReadyMapper;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        log.info("Data-Added event received with correlationId: {}", correlationId);
        try {
            final AdapterEvent adapterEvent = parseMessage(ccsiQueueMessageContext.getMessage().getBody());
            final GtvAccountCreationEvent gtvAccountCreationEvent = gtvAccountReadyMapper.map((DataMappingStoredEvent) adapterEvent);
            gtvDataReadyPublishService.publishMessageToQueue(this.parseMessage(gtvAccountCreationEvent), SqsUtils.createMessageAttributesWithCorrelationId(correlationId));
        }catch (JsonProcessingException jpe){
            log.error("Couldn't parse message body for event with correlationId {} Cause: {}", correlationId, jpe.getMessage());
            return CCSIQueueMessageResult.builder()
                    .status(CCSIQueueMessageStatus.NON_RECOVERABLE_ERROR)
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

    private AdapterEvent parseMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, AdapterEvent.class);
    }

    //GtvAccountCreationEvent to json string
    private String parseMessage(GtvAccountCreationEvent gtvAccountCreationEvent) throws JsonProcessingException {
        return objectMapper.writeValueAsString(gtvAccountCreationEvent);
    }
}
