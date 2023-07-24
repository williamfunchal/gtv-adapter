package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.api.config.QueueProperties;
import com.consensus.gtvadapter.util.SqsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;

@Slf4j
@Component
public class AddDataProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final StoredPublishService storedPublishService;

    public AddDataProcessor(final QueueProperties queueProperties, final StoredPublishService storedPublishService) {
        this.properties = queueProperties.getAddData();
        this.storedPublishService = storedPublishService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final String correlationId = ccsiQueueMessageContext.getCorrelationId();
        log.info("Add-Data event received with correlationId: {}", correlationId);
        storedPublishService.publishMessageToQueue(ccsiQueueMessageContext.getMessage().getBody(), getMessageAttributes(correlationId));
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
