package com.consensus.gtvadapter.repository.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataReadyToUpdateProcessor implements QueueMessageProcessor {

    private final CCSIQueueListenerProperties queueProperties;

    public DataReadyToUpdateProcessor(QueueProperties queueProperties) {
        this.queueProperties = queueProperties.getUpdateData();
    }

    @Override
    public CCSIQueueListenerProperties getQueueProperties() {
        return this.queueProperties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext messageContext) {
        log.debug("Processing SQS message of type: {}", messageContext.getEventType());

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }
}
