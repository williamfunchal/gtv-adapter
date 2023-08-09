package com.consensus.gtvadapter.module.processor.service;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtvadapter.common.sqs.CCSIAbstractQueuePublishService;
import com.consensus.gtvadapter.config.QueueProperties;


@Service
public class AdapterGtvDataReadyPublishService extends CCSIAbstractQueuePublishService {

    private CCSIQueueProperties queueProperties;

    public AdapterGtvDataReadyPublishService(final AmazonSQS amazonSQS, final QueueProperties queueProperties) {
        super(amazonSQS);
        this.queueProperties = queueProperties.getAdapterGtvDataReady();
    }

    @Override
    public CCSIQueueProperties getQueueProperties() {
        return this.queueProperties;
    }
}
