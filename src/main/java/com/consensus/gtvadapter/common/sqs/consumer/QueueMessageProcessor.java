package com.consensus.gtvadapter.common.sqs.consumer;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;

public interface QueueMessageProcessor {

    CCSIQueueMessageResult process(CCSIQueueMessageContext message);

    CCSIQueueListenerProperties getQueueProperties();
}
