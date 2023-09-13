package com.consensus.gtvadapter.processor.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageBatchProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.service.EventProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.consensus.gtvadapter.util.GtvConstants.SqsMessageAttributes.EVENT_TYPE;

@Slf4j
@Component
public class IspDataReadyProcessor extends BaseDataReadyProcessor implements QueueMessageBatchProcessor {

    private static final Set<String> MESSAGE_BATCH_GROUPS = Set.of("usage-isp-new");

    public IspDataReadyProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            DataReadyToStorePublishService dataReadyToStorePublishService,
            EventProcessingService eventProcessingService) {
        super(objectMapper, queueProperties, dataReadyToStorePublishService, eventProcessingService);
    }

    /**
     * Process usage events in batches and all other types one by one
     */
    @Override
    public CCSIQueueMessageResult process(String groupName, List<CCSIQueueMessageContext> messages) {
        log.debug("Processing usage events batch: {}", messages.size());
        // TODO CUP-19: Implement logic to process batch of usage messages from Data-Ready-Queue
        /*
         * 1) 'messages' - already filtered and contains only usage events
         * 2) Convert 'messages' into one usage batch event
         * 3) Send it to store queue
         * */

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    @Override
    public Set<String> getBatchGroups() {
        return MESSAGE_BATCH_GROUPS;
    }

    @Override
    public String messageGroupId(Message message) {
        return Optional.ofNullable(message.getMessageAttributes())
                .map(attrs -> attrs.get(EVENT_TYPE))
                .map(MessageAttributeValue::getStringValue)
                .filter(MESSAGE_BATCH_GROUPS::contains)
                .orElseGet(() -> super.messageGroupId(message));
    }
}
