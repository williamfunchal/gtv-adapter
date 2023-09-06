package com.consensus.gtvadapter.common;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.consensus.common.sqs.CCSIQueueProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class CCSIAbstractQueuePublishService implements CCSIQueuePublishService{

    private final AmazonSQS amazonSQS;

    @Override
    public SendMessageResult publishMessageToQueue(String message,  Map<String, MessageAttributeValue> attributes, String messageGroupId){
        Assert.hasText(message, "Message is empty");

        log.debug("Publishing message to SQS {}", message);
        final CCSIQueueProperties queueProperties = getQueueProperties();
        final SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueProperties.getQueueUrl())
                .withMessageBody(message)
                .withMessageAttributes(attributes)
                .withMessageGroupId(messageGroupId);

        return amazonSQS.sendMessage(sendMessageRequest);
    }
}
