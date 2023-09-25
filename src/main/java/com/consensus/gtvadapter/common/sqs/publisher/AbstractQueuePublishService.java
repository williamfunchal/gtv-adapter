package com.consensus.gtvadapter.common.sqs.publisher;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.consensus.common.sqs.CCSIQueueProperties;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.consensus.gtvadapter.util.SqsUtils.createMessageAttributes;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractQueuePublishService<T extends AdapterEvent> implements QueuePublishService<T> {

    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;
    private final CCSIQueueProperties queueProperties;

    @Override
    public SendMessageResult publishMessage(AdapterEvent event) {
        SendMessageRequest sendMessageRequest = createSendRequest(event);
        String messageBody = sendMessageRequest.getMessageBody();

        log.debug("Publishing message to '{}' SQS queue: {}", queueProperties.getQueueShortName(), messageBody);
        SendMessageResult result = amazonSQS.sendMessage(sendMessageRequest);
        log.debug("Message published to '{}' SQS queue: {}", queueProperties.getQueueShortName(), messageBody);

        return result;
    }

    @SneakyThrows
    protected SendMessageRequest createSendRequest(AdapterEvent event) {
        return new SendMessageRequest()
                .withQueueUrl(queueProperties.getQueueUrl())
                .withMessageBody(objectMapper.writeValueAsString(event))
                .withMessageGroupId(event.getGroupId())
                .withMessageDeduplicationId(event.getEventId())
                .withMessageAttributes(createMessageAttributes(event));
    }
}
