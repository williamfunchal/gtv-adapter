package com.consensus.gtvadapter.processor.sqs.isp_data_ready;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.mapper.ProcessorMapperService;
import com.consensus.gtvadapter.processor.sqs.StoreDataPublishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UsageDataReadyProcessor {

    private final ObjectMapper objectMapper;
    private final CCSIQueueListenerProperties queueProperties;
    private final StoreDataPublishService storeDataPublishService;
    private final ProcessorMapperService processorMapperService;

    public UsageDataReadyProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            StoreDataPublishService storeDataPublishService, ProcessorMapperService processorMapperService) {
        this.objectMapper = objectMapper;
        this.queueProperties = queueProperties.getIspDataReady();
        this.storeDataPublishService = storeDataPublishService;
        this.processorMapperService = processorMapperService;
    }

    public CCSIQueueMessageResult process(List<CCSIQueueMessageContext> messages) {
        log.debug("Processing usage events batch: {}", messages.size());
        // TODO CUP-19: Implement logic to process batch of usage messages from Data-Ready-Queue

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }
}
