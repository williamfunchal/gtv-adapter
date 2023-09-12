package com.consensus.gtvadapter.common.sqs.listener;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class QueueMessageBatchListener extends QueueMessageListener {

    private final QueueMessageBatchProcessor queueMessageBatchProcessor;

    public QueueMessageBatchListener(AmazonSQS amazonSQS, QueueMessageBatchProcessor queueMessageBatchProcessor,
            QueueMessageContextCreator queueMessageContextCreator) {
        super(amazonSQS, queueMessageBatchProcessor, queueMessageContextCreator);
        this.queueMessageBatchProcessor = queueMessageBatchProcessor;
    }

    @Override
    protected AsyncMessageListener createMessageListener() {
        return new AsyncMessageBatchListener();
    }

    protected void handleMessageBatch(String groupName, List<Message> messages) {
        try {
            var context = messages.stream().map(queueMessageContextCreator::createMessageContext).collect(toList());
            var result = queueMessageBatchProcessor.process(groupName, context);

            if (queueProperties.isRouteToDlq() && result.getStatus().isErrorStatus() || result.getStatus().isSendToDlq()) {
                redirectToDeadLetterQueueBatch(messages);
            } else if (queueProperties.isAutoDelete() && result.getStatus().isFinalStatus()) {
                deleteMessageBatch(messages);
            }
        } catch (Exception ex) {
            log.error("Exception processing sqs message: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private void redirectToDeadLetterQueueBatch(List<Message> messages) {
        String deadLetterQueueUrl = queueProperties.getDeadLetterQueueUrl();
        if (isBlank(deadLetterQueueUrl)) {
            return;
        }

        List<SendMessageBatchRequestEntry> sendRequests = messages.stream()
                .map(message -> new SendMessageBatchRequestEntry()
                        .withMessageBody(message.getBody())
                        .withMessageGroupId(messageProcessor.messageGroupId(message))
                        .withMessageDeduplicationId(messageProcessor.messageDeduplicationId(message))
                        .withMessageAttributes(message.getMessageAttributes()))
                .collect(toList());

        amazonSQS.sendMessageBatch(new SendMessageBatchRequest(queueProperties.getQueueUrl(), sendRequests));

        deleteMessageBatch(messages);

        log.info("SQS messages were redirected to dead-letter queue: {}.", deadLetterQueueUrl);
    }

    private void deleteMessageBatch(List<Message> messages) {
        List<DeleteMessageBatchRequestEntry> deleteRequests = messages.stream()
                .map(msg -> new DeleteMessageBatchRequestEntry(msg.getMessageId(), msg.getReceiptHandle()))
                .collect(toList());

        amazonSQS.deleteMessageBatch(new DeleteMessageBatchRequest(queueProperties.getQueueUrl(), deleteRequests));
    }

    protected class AsyncMessageBatchListener extends AsyncMessageListener {

        @Override
        protected MessageRunnableGroup groupByMessageGroupId(List<Message> group) {
            List<Runnable> runnableGroup = group.stream()
                    .collect(groupingBy(messageProcessor::messageGroupId, toList()))
                    .entrySet().stream()
                    .map(entry -> messageGroupHandler(entry.getKey(), entry.getValue()))
                    .collect(toList());
            return new MessageRunnableGroup(queueProperties.getQueueShortName(), runnableGroup);
        }

        protected Runnable messageGroupHandler(String groupName, List<Message> messages) {
            Set<String> batchGroups = queueMessageBatchProcessor.getBatchGroups();
            if (batchGroups.contains(groupName) || batchGroups.contains("All")) {
                return () -> handleMessageBatch(groupName, messages);
            } else {
                return super.messageGroupHandler(groupName, messages);
            }
        }
    }
}
