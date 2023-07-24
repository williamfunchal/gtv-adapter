package com.consensus.gtvadapter.api.sqs;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessage;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageProcessor;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.api.config.QueueProperties;
import com.consensus.gtvadapter.api.models.response.UsageEventsBulkResponse;
import com.consensus.gtvadapter.api.service.GtvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiReadyProcessor implements CCSIQueueMessageProcessor {

    private final CCSIQueueListenerProperties properties;
    private final GtvService gtvService;

    public ApiReadyProcessor(final QueueProperties queueProperties, final GtvService gtvService) {
        this.properties = queueProperties.getApi();
        this.gtvService = gtvService;
    }

    @Override
    public CCSIQueueListenerProperties getQueueListenerProperties() {
        return this.properties;
    }

    @Override
    public CCSIQueueMessageResult process(CCSIQueueMessageContext ccsiQueueMessageContext) {
        final CCSIQueueMessage message = ccsiQueueMessageContext.getMessage();
        log.info("Api-Ready event received with correlationId: {} and message body: {}", ccsiQueueMessageContext.getCorrelationId(), message.getBody());
        final UsageEventsBulkResponse usageEventsBulkResponse = gtvService.createUsageBulk(message.getBody()).orElseThrow(IllegalStateException::new);
        log.info("GTV API response: {}", usageEventsBulkResponse);
        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }
}
