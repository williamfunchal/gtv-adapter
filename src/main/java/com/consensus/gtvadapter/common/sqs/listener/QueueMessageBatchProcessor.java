package com.consensus.gtvadapter.common.sqs.listener;

import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;

public interface QueueMessageBatchProcessor extends QueueMessageProcessor {

    CCSIQueueMessageResult process(String groupName, List<CCSIQueueMessageContext> messages);

    default Set<String> getBatchGroups() {
        return emptySet();
    }
}
