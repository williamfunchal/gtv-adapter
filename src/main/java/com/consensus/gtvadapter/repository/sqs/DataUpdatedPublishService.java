package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtvadapter.config.QueueProperties;
import com.consensus.gtvadapter.common.CCSIAbstractQueuePublishService;
import org.springframework.stereotype.Service;

@Service
public class DataUpdatedPublishService extends CCSIAbstractQueuePublishService {

    private CCSIQueueProperties queueProperties;

    public DataUpdatedPublishService(final AmazonSQS amazonSQS, final QueueProperties queueProperties) {
        super(amazonSQS);
        this.queueProperties = queueProperties.getDataUpdated();
    }

    @Override
    public CCSIQueueProperties getQueueProperties() {
        return this.queueProperties;
    }
}