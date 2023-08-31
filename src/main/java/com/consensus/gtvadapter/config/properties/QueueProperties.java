package com.consensus.gtvadapter.config.properties;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws.sqs")
public class QueueProperties {

    private CCSIQueueListenerProperties ispDataReady;
    private CCSIQueueListenerProperties updateData;
    private CCSIQueueListenerProperties dataUpdated;
    private CCSIQueueListenerProperties storeData;
    private CCSIQueueListenerProperties dataStored;
    private CCSIQueueListenerProperties gtvRequest;
    private CCSIQueueListenerProperties gtvResponse;
}
