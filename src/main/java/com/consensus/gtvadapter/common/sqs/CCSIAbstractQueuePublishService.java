package com.consensus.gtvadapter.common.sqs;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.consensus.common.sqs.CCSIQueueProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

@Slf4j
public abstract class CCSIAbstractQueuePublishService implements CCSIQueuePublishService{

    private final AmazonSQS amazonSQS;

    public CCSIAbstractQueuePublishService(AmazonSQS amazonSQS) {
        Assert.notNull(amazonSQS, "amazonSQS cannot be null");
        this.amazonSQS = amazonSQS;
    }

    @Override
    public SendMessageResult publishMessageToQueue(@NotEmpty String message,  Map<String, MessageAttributeValue> attributes){

        log.debug("Publishing message to SQS {}", message);
        final CCSIQueueProperties queueProperties = getQueueProperties();
        final SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueProperties.getQueueUrl()).withMessageBody(message).withMessageAttributes(attributes);

        return amazonSQS.sendMessage(sendMessageRequest);
    }
}
