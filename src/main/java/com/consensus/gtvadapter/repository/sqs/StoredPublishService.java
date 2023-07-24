package com.consensus.gtvadapter.repository.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtvadapter.api.config.QueueProperties;
import com.consensus.gtvadapter.common.CCSIAbstractQueuePublishService;
import org.springframework.stereotype.Service;

@Service
public class StoredPublishService extends CCSIAbstractQueuePublishService {

    private CCSIQueueProperties queueProperties;

    public StoredPublishService(final AmazonSQS amazonSQS, final QueueProperties queueProperties) {
        super(amazonSQS);
        this.queueProperties = queueProperties.getDataStored();
    }

    @Override
    public CCSIQueueProperties getQueueProperties() {
        return this.queueProperties;
    }
}
