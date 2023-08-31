package com.consensus.gtvadapter.gateway.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtvadapter.common.CCSIAbstractQueuePublishService;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import org.springframework.stereotype.Service;

@Service
public class GtvResponsePublishService extends CCSIAbstractQueuePublishService {

    private CCSIQueueProperties queueProperties;

    public GtvResponsePublishService(final AmazonSQS amazonSQS, final QueueProperties queueProperties) {
        super(amazonSQS);
        this.queueProperties = queueProperties.getGtvResponse();
    }

    @Override
    public CCSIQueueProperties getQueueProperties() {
        return this.queueProperties;
    }
}
