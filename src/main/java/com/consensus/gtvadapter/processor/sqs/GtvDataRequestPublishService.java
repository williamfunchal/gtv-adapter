package com.consensus.gtvadapter.processor.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.gtv.request.BaseGtvRequest;
import com.consensus.gtvadapter.common.sqs.publisher.AbstractQueuePublishService;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class GtvDataRequestPublishService<T extends BaseGtvRequest> extends AbstractQueuePublishService<T> {

    public GtvDataRequestPublishService(AmazonSQS amazonSQS, ObjectMapper objectMapper, QueueProperties queueProperties) {
        super(amazonSQS, objectMapper, queueProperties.getGtvRequest());
    }
}
