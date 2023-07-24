package com.consensus.gtvadapter.api.config;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws.sqs")
public class QueueProperties {
    private CCSIQueueListenerProperties api;
    private CCSIQueueListenerProperties updateData;
    private CCSIQueueListenerProperties addData;
    private CCSIQueueListenerProperties dataUpdated;
    private CCSIQueueListenerProperties dataStored;
    private CCSIQueueListenerProperties ispData;
}
