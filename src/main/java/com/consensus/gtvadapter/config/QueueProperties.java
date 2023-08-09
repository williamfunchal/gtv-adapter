package com.consensus.gtvadapter.config;

import com.consensus.common.sqs.CCSIQueueListenerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws.sqs")
public class QueueProperties {
    private CCSIQueueListenerProperties adapterIspDataReady;
    private CCSIQueueListenerProperties adapterDataReadyToUpdate;
    private CCSIQueueListenerProperties adapterDataUpdated;
    private CCSIQueueListenerProperties adapterDataReadyToStore;
    private CCSIQueueListenerProperties adapterDataStored;
    private CCSIQueueListenerProperties adapterGtvDataReady;
    private CCSIQueueListenerProperties gtvResponseReceived;
}
