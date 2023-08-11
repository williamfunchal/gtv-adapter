package com.consensus.gtvadapter.module.storage.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.config.QueueProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdapterDataReadyToUpdateProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;

    @Autowired
    public AdapterDataReadyToUpdateProcessor(final QueueProperties queueProperties) {
        this.properties = queueProperties.getAdapterDataReadyToUpdate();
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        log.info("Data received for UPDATE");
        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }
}
