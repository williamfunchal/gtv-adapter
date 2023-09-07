package com.consensus.gtvadapter.common.sqs;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;

public interface QueuePublishService<T extends AdapterEvent> {

    SendMessageResult publishMessage(T event);
}
