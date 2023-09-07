package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.sqs.AbstractQueuePublishService;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class DataUpdatedPublishService<T extends AdapterEvent> extends AbstractQueuePublishService<T> {

    public DataUpdatedPublishService(AmazonSQS amazonSQS, ObjectMapper objectMapper, QueueProperties queueProperties) {
        super(amazonSQS, objectMapper, queueProperties.getDataUpdated());
    }
}