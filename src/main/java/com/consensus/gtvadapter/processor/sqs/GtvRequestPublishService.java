package com.consensus.gtvadapter.processor.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtvadapter.config.QueueProperties;
import com.consensus.gtvadapter.common.CCSIAbstractQueuePublishService;
import org.springframework.stereotype.Service;


@Service
public class GtvRequestPublishService extends CCSIAbstractQueuePublishService {

    private CCSIQueueProperties queueProperties;

    public GtvRequestPublishService(final AmazonSQS amazonSQS, final QueueProperties queueProperties) {
        super(amazonSQS);
        this.queueProperties = queueProperties.getGtvRequest();
    }

    @Override
    public CCSIQueueProperties getQueueProperties() {
        return this.queueProperties;
    }
}
