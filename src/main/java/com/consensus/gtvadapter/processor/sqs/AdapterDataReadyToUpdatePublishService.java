package com.consensus.gtvadapter.processor.sqs;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.sqs.publisher.AbstractQueuePublishService;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AdapterDataReadyToUpdatePublishService <T extends AdapterEvent> extends AbstractQueuePublishService<T> {

    public AdapterDataReadyToUpdatePublishService(final AmazonSQS amazonSQS,ObjectMapper objectMapper, final QueueProperties queueProperties) {
        super(amazonSQS, objectMapper, queueProperties.getAdapterDataReadyToUpdate());
    }
}
