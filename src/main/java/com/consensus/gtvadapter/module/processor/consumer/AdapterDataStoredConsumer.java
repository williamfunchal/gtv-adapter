package com.consensus.gtvadapter.module.processor.consumer;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.config.QueueProperties;
import com.consensus.gtvadapter.module.processor.service.AdapterGtvDataReadyPublishService;
import com.consensus.gtvadapter.util.SqsUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdapterDataStoredConsumer implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final AdapterGtvDataReadyPublishService adapterGtvDataReadyPublishService;

    @Autowired
    public AdapterDataStoredConsumer(final QueueProperties queueProperties, final AdapterGtvDataReadyPublishService adapterGtvDataReadyPublishService) {
        this.properties = queueProperties.getAdapterDataStored();
        this.adapterGtvDataReadyPublishService = adapterGtvDataReadyPublishService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        log.info("Data-Stored event received with correlationId: {}", correlationId);
        adapterGtvDataReadyPublishService.publishMessageToQueue(ccsiQueueMessageContext.getMessage().getBody(), getMessageAttributes(correlationId));
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
}
