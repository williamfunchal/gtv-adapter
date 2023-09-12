package com.consensus.gtvadapter.common.sqs.listener;

import com.amazonaws.services.sqs.model.Message;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;

import static com.amazonaws.services.sqs.model.MessageSystemAttributeName.MessageDeduplicationId;
import static com.amazonaws.services.sqs.model.MessageSystemAttributeName.MessageGroupId;

public interface QueueMessageProcessor {

    CCSIQueueMessageResult process(CCSIQueueMessageContext message);

    CCSIQueueListenerProperties getQueueProperties();

    default String messageGroupId(Message message) {
        return message.getAttributes().get(MessageGroupId.toString());
    }

    default String messageDeduplicationId(Message message) {
        return message.getAttributes().get(MessageDeduplicationId.toString());
    }
}
