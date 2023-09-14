package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.sqs.publisher.AbstractQueuePublishService;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class DataUpdatedQueuePublishService<T extends AdapterEvent> extends AbstractQueuePublishService<T> {

    public DataUpdatedQueuePublishService(AmazonSQS amazonSQS, ObjectMapper objectMapper, QueueProperties queueProperties) {
        super(amazonSQS, objectMapper, queueProperties.getDataUpdated());
    }
}