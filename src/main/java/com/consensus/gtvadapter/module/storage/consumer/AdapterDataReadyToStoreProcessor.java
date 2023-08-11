package com.consensus.gtvadapter.module.storage.consumer;

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
import com.consensus.gtvadapter.module.storage.services.AdapterDataStoredPublishService;
import com.consensus.gtvadapter.util.SqsUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdapterDataReadyToStoreProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final AdapterDataStoredPublishService adapterAdapterDataStoredPublishService;

    @Autowired
    public AdapterDataReadyToStoreProcessor(final QueueProperties queueProperties, final AdapterDataStoredPublishService adapterAdapterDataStoredPublishService) {
        this.properties = queueProperties.getAdapterDataReadyToStore();
        this.adapterAdapterDataStoredPublishService = adapterAdapterDataStoredPublishService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        log.info("Add-Data event received with correlationId: {}", correlationId);
        adapterAdapterDataStoredPublishService.publishMessageToQueue(ccsiQueueMessageContext.getMessage().getBody(), getMessageAttributes(correlationId));
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
