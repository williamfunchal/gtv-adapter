package com.consensus.gtvadapter.processor.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.sqs.AbstractQueuePublishService;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class StoreDataPublishService<T extends AdapterEvent> extends AbstractQueuePublishService<T> {

    public StoreDataPublishService(AmazonSQS amazonSQS, ObjectMapper objectMapper, QueueProperties queueProperties) {
        super(amazonSQS, objectMapper, queueProperties.getStoreData());
    }
}