package com.consensus.gtvadapter.module.storage.services.sqs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtvadapter.common.sqs.CCSIAbstractQueuePublishService;
import com.consensus.gtvadapter.config.QueueProperties;

@Service
public class AdapterDataStoredPublishService extends CCSIAbstractQueuePublishService {

    private CCSIQueueProperties queueProperties;

    @Autowired
    public AdapterDataStoredPublishService(final AmazonSQS amazonSQS, final QueueProperties queueProperties) {
        super(amazonSQS);
        this.queueProperties = queueProperties.getAdapterDataStored();
    }

    @Override
    public CCSIQueueProperties getQueueProperties() {
        return this.queueProperties;
    }
}
