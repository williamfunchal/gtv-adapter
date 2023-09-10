package com.consensus.gtvadapter.common.sqs.consumer;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.gtvadapter.util.SqsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static com.amazonaws.services.sqs.model.MessageSystemAttributeName.MessageDeduplicationId;
import static com.amazonaws.services.sqs.model.MessageSystemAttributeName.MessageGroupId;
import static com.consensus.gtvadapter.common.sqs.consumer.ThreadingUtils.queueTaskListener;
import static com.consensus.gtvadapter.common.sqs.consumer.ThreadingUtils.queueTaskProcessor;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
public class QueueMessageListener implements DisposableBean, SmartLifecycle {

    protected final AmazonSQS amazonSQS;
    protected final AsyncTaskExecutor queueTaskListener;
    protected final AsyncTaskExecutor queueTaskProcessor;
    protected final QueueMessageProcessor messageProcessor;
    protected final QueueMessageContextCreator queueMessageContextCreator;

    protected final CCSIQueueListenerProperties queueProperties;
    protected final boolean isFifo;

    private volatile boolean running;
    private Future<?> listener;

    private final Object listenerMonitor = new Object();

    public QueueMessageListener(AmazonSQS amazonSQS, QueueMessageProcessor messageProcessor,
            QueueMessageContextCreator queueMessageContextCreator) {
        this.queueProperties = messageProcessor.getQueueProperties();
        this.amazonSQS = amazonSQS;
        this.queueTaskListener = queueTaskListener(queueProperties);
        this.queueTaskProcessor = queueTaskProcessor(queueProperties);
        this.messageProcessor = messageProcessor;
        this.queueMessageContextCreator = queueMessageContextCreator;
        this.isFifo = SqsUtils.isFifo(queueProperties.getQueueUrl());
    }

    @Override
    public void start() {
        doStart();
    }

    private void doStart() {
        if (running) {
            return;
        }
        running = true;
        synchronized (listenerMonitor) {
            listener = queueTaskListener.submit(createMessageListener());
        }
    }

    @Override
    public void stop() {
        doStop();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void destroy() {
        doStop();
    }

    private void doStop() {
        running = false;
        if (listener == null) {
            return;
        }
        synchronized (listenerMonitor) {
            if (listener != null) {
                try {
                    // Wait for the task to finish
                    listener.get(queueProperties.getWaitTimeSeconds(), SECONDS);
                } catch (ExecutionException | TimeoutException ex) {
                    log.warn("An exception occurred while stopping queue: {}", queueProperties.getQueueShortName(), ex);
                } catch (InterruptedException ex) {
                    currentThread().interrupt();
                }
            }
        }
    }

    protected AsyncMessageListener createMessageListener() {
        return new AsyncMessageListener();
    }

    protected void handleMessage(Message message) {
        try {
            CCSIQueueMessageContext context = queueMessageContextCreator.createMessageContext(message);
            CCSIQueueMessageResult result = messageProcessor.process(context);

            if (queueProperties.isRouteToDlq() && result.getStatus().isErrorStatus() || result.getStatus().isSendToDlq()) {
                redirectToDeadLetterQueue(message);
            } else if (queueProperties.isAutoDelete() && result.getStatus().isFinalStatus()) {
                deleteMessage(message.getReceiptHandle());
            }
        } catch (Exception ex) {
            log.error("Exception processing sqs message: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private void redirectToDeadLetterQueue(Message message) {
        String deadLetterQueueUrl = queueProperties.getDeadLetterQueueUrl();
        if (isBlank(deadLetterQueueUrl)) {
            return;
        }

        String jsonBody = message.getBody();

        var dlqRequest = new SendMessageRequest()
                .withQueueUrl(deadLetterQueueUrl)
                .withMessageBody(jsonBody)
                .withMessageGroupId(messageGroupId(message))
                .withMessageDeduplicationId(messageDeduplicationId(message))
                .withMessageAttributes(message.getMessageAttributes());

        amazonSQS.sendMessage(dlqRequest);

        deleteMessage(message.getReceiptHandle());

        log.info("SQS message was redirected to dead-letter queue: {}.", deadLetterQueueUrl);
    }

    protected void deleteMessage(String receiptHandle) {
        amazonSQS.deleteMessage(new DeleteMessageRequest(queueProperties.getQueueUrl(), receiptHandle));
    }

    protected static String messageGroupId(Message message) {
        return message.getAttributes().get(MessageGroupId.toString());
    }

    protected static String messageDeduplicationId(Message message) {
        return message.getAttributes().get(MessageDeduplicationId.toString());
    }

    protected class AsyncMessageListener implements Runnable {

        @Override
        public void run() {
            while (running) {
                try {
                    doHandle();
                } catch (InterruptedException ex) {
                    log.info("The process was interrupted but there was no messages in {}", queueProperties.getQueueShortName());
                    currentThread().interrupt();
                } catch (Exception ex) {
                    log.error("Queue listener processing exception. Trying to restore.", ex);
                    try {
                        Thread.sleep(queueProperties.getRetryDelay());
                    } catch (InterruptedException iex) {
                        currentThread().interrupt();
                    }
                }
            }
        }

        protected void doHandle() throws InterruptedException {
            var messages = amazonSQS.receiveMessage(createMessageRequest()).getMessages();
            if (isEmpty(messages)) {
                log.info("No messages in: {}", queueProperties.getQueueShortName());
                Thread.sleep(queueProperties.getEmptyReceivedWaitTimeMil());
                return;
            } else {
                log.info("Received {} messages from: {}", messages.size(), queueProperties.getQueueShortName());
            }

            var runnableGroup = createMessageHandlerGroup(messages);

            for (var runnable : runnableGroup) {
                if (running) {
                    queueTaskProcessor.execute(runnable);
                }
            }
        }

        protected MessageRunnableGroup createMessageHandlerGroup(List<Message> messages) {
            if (isFifo) {
                return groupByMessageGroupId(messages);
            } else {
                return groupBySingleMessage(messages);
            }
        }

        protected MessageRunnableGroup groupByMessageGroupId(List<Message> group) {
            return new MessageRunnableGroup(queueProperties.getQueueShortName(), group.stream()
                    .collect(groupingBy(QueueMessageListener::messageGroupId,
                            collectingAndThen(toList(), this::messageGroupHandler)))
                    .values());
        }

        protected MessageRunnableGroup groupBySingleMessage(List<Message> messages) {
            return messages.stream()
                    .map(this::messageHandler)
                    .collect(collectingAndThen(toList(), list -> new MessageRunnableGroup(queueProperties.getQueueShortName(), list)));
        }

        protected Runnable messageGroupHandler(List<Message> messages) {
            return () -> messages.forEach(QueueMessageListener.this::handleMessage);
        }

        protected Runnable messageHandler(Message message) {
            return () -> QueueMessageListener.this.handleMessage(message);
        }

        protected ReceiveMessageRequest createMessageRequest() {
            return new ReceiveMessageRequest()
                    .withQueueUrl(queueProperties.getQueueUrl())
                    .withAttributeNames(queueProperties.getAttributeNames())
                    .withMessageAttributeNames(queueProperties.getMessageAttributeNames())
                    .withMaxNumberOfMessages(queueProperties.getBatchSize())
                    .withVisibilityTimeout(queueProperties.getVisibilityTimeout())
                    .withWaitTimeSeconds(queueProperties.getWaitTimeSeconds());
        }
    }
}
