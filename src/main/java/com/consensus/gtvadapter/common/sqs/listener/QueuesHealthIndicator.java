package com.consensus.gtvadapter.common.sqs.listener;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.consensus.common.sqs.CCSIQueueListenerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RequiredArgsConstructor
@Component("sqsQueueHealthIndicator")
@ConditionalOnBean(QueueMessageProcessor.class)
public class QueuesHealthIndicator implements ReactiveHealthIndicator {

    private static final String NUMBER_OF_MESSAGES = "ApproximateNumberOfMessages";
    private static final String NUMBER_OF_MESSAGES_NOT_VISIBLE = "ApproximateNumberOfMessagesNotVisible";

    private final AmazonSQS amazonSQS;
    private final List<QueueMessageProcessor> messageQueues;

    @Override
    public Mono<Health> health() {
        return Mono.fromCallable(this::buildHealth).subscribeOn(Schedulers.boundedElastic());
    }

    public Health buildHealth() {
        Health.Builder healthBuilder = Health.up();
        Map<String, Object> details = new TreeMap<>();
        try {
            messageQueues.forEach(queues -> addQueueMetrics(queues.getQueueProperties(), details));
        } catch (Exception ex) {
            log.error("Exception during SQS health validation.", ex);
            healthBuilder = healthBuilder.down().withDetail("error_cause", "SQS connection error");
        }

        return healthBuilder.withDetails(details).build();
    }

    private void addQueueMetrics(CCSIQueueListenerProperties properties, Map<String, Object> details) {
        Map<String, String> queueDetails = getSizeAttributes(properties.getQueueUrl());
        Map<String, Object> metadata = new TreeMap<>();
        metadata.put("queue-url", properties.getQueueUrl());
        metadata.put("queue-metadata", queueDetails);
        details.put(properties.getQueueShortName(), metadata);
    }

    private Map<String, String> getSizeAttributes(String queueUrl) {
        return amazonSQS
                .getQueueAttributes(new GetQueueAttributesRequest(queueUrl).withAttributeNames(NUMBER_OF_MESSAGES, NUMBER_OF_MESSAGES_NOT_VISIBLE))
                .getAttributes();
    }

}

