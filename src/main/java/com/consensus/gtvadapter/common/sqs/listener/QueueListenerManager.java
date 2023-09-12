package com.consensus.gtvadapter.common.sqs.listener;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@ConditionalOnBean(QueueMessageProcessor.class)
public class QueueListenerManager {

    private final Map<String, QueueMessageListener> queueListeners;

    public QueueListenerManager(AmazonSQS amazonSQS, List<QueueMessageProcessor> messageProcessors,
            QueueMessageContextCreator queueMessageContextCreator) {
        this.queueListeners = Optional.ofNullable(messageProcessors)
                .orElse(emptyList())
                .stream()
                .collect(Collectors.toMap(
                        queue -> queue.getQueueProperties().getQueueUrl(),
                        queue -> createEventListener(amazonSQS, queue, queueMessageContextCreator)
                ));
    }

    protected QueueMessageListener createEventListener(AmazonSQS amazonSQS, QueueMessageProcessor processor,
            QueueMessageContextCreator queueMessageContextCreator) {
        if (processor instanceof QueueMessageBatchProcessor) {
            return new QueueMessageBatchListener(amazonSQS, (QueueMessageBatchProcessor) processor, queueMessageContextCreator);
        } else {
            return new QueueMessageListener(amazonSQS, processor, queueMessageContextCreator);
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void subscribe() {
        if (!isEmpty(queueListeners)) {
            queueListeners.values().forEach(QueueMessageListener::start);
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void unsubscribe() {
        if (!isEmpty(queueListeners)) {
            queueListeners.values().forEach(QueueMessageListener::stop);
        }
    }
}
