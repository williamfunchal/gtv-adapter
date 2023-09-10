package com.consensus.gtvadapter.processor.sqs.isp_data_ready;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.consensus.gtvadapter.common.sqs.consumer.MessageRunnableGroup;
import com.consensus.gtvadapter.common.sqs.consumer.QueueMessageContextCreator;
import com.consensus.gtvadapter.common.sqs.consumer.QueueMessageListener;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.consensus.gtvadapter.util.GtvConstants.SqsMessageAttributes.EVENT_TYPE;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class DataReadyQueueMessageListener extends QueueMessageListener {

    private static final String USAGE_EVENT_TYPE = "usage-new";

    private final UsageDataReadyProcessor usageDataReadyProcessor;

    public DataReadyQueueMessageListener(AmazonSQS amazonSQS,
            CustomerDataReadyProcessor customerDataReadyProcessor,
            UsageDataReadyProcessor usageDataReadyProcessor,
            QueueMessageContextCreator queueMessageContextCreator) {
        super(amazonSQS, customerDataReadyProcessor, queueMessageContextCreator);
        this.usageDataReadyProcessor = usageDataReadyProcessor;
    }

    @Override
    protected AsyncMessageListener createMessageListener() {
        return new IspDataReadyAsyncMessageListener();
    }

    protected void handleMessageBatch(List<Message> messages) {
        try {
            var context = messages.stream().map(queueMessageContextCreator::createMessageContext).collect(toList());
            var result = usageDataReadyProcessor.process(context);

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
                        .withMessageGroupId(messageGroupId(message))
                        .withMessageDeduplicationId(messageDeduplicationId(message))
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

    protected class IspDataReadyAsyncMessageListener extends AsyncMessageListener {

        @Override
        protected MessageRunnableGroup groupByMessageGroupId(List<Message> messageBatch) {
            List<Message> usages = new ArrayList<>();
            List<Message> other = new ArrayList<>();
            for (Message message : messageBatch) {
                var eventTypeAttr = extractEventType(message);
                if (USAGE_EVENT_TYPE.equals(eventTypeAttr)) {
                    usages.add(message);
                } else {
                    other.add(message);
                }
            }

            Map<String, Runnable> messageGroups = other.stream()
                    .collect(groupingBy(QueueMessageListener::messageGroupId,
                            HashMap::new,
                            collectingAndThen(toList(), this::messageGroupHandler))
                    );
            if (!usages.isEmpty()) {
                messageGroups.put("usage", messageUsageBatchHandler(usages));
            }

            return new MessageRunnableGroup(queueProperties.getQueueShortName(), messageGroups.values());
        }

        private String extractEventType(Message message) {
            return Optional.ofNullable(message.getMessageAttributes())
                    .map(attrs -> attrs.get(EVENT_TYPE))
                    .map(MessageAttributeValue::getStringValue)
                    .orElse(null);
        }

        private Runnable messageUsageBatchHandler(List<Message> messages) {
            return () -> DataReadyQueueMessageListener.this.handleMessageBatch(messages);
        }
    }
}
