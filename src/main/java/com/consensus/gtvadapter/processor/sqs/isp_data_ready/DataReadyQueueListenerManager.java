package com.consensus.gtvadapter.processor.sqs.isp_data_ready;

import com.amazonaws.services.sqs.AmazonSQS;
import com.consensus.gtvadapter.common.sqs.consumer.QueueMessageContextCreator;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.mapper.ProcessorMapperService;
import com.consensus.gtvadapter.processor.sqs.StoreDataPublishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataReadyQueueListenerManager {

    private final DataReadyQueueMessageListener dataReadyQueueMessageListener;

    public DataReadyQueueListenerManager(AmazonSQS amazonSQS, ObjectMapper objectMapper,
            QueueProperties queueProperties, StoreDataPublishService storeDataPublishService,
            ProcessorMapperService processorMapperService, QueueMessageContextCreator queueMessageContextCreator) {
        this.dataReadyQueueMessageListener = new DataReadyQueueMessageListener(
                amazonSQS,
                createCustomerDataReadyProcessor(objectMapper, queueProperties, storeDataPublishService, processorMapperService),
                createUsageDataReadyProcessor(objectMapper, queueProperties, storeDataPublishService, processorMapperService),
                queueMessageContextCreator
        );
    }

    @EventListener(ContextRefreshedEvent.class)
    public void subscribe() {
        dataReadyQueueMessageListener.start();
    }

    @EventListener(ContextClosedEvent.class)
    public void unsubscribe() {
        dataReadyQueueMessageListener.stop();
    }

    private CustomerDataReadyProcessor createCustomerDataReadyProcessor(ObjectMapper objectMapper,
            QueueProperties queueProperties,
            StoreDataPublishService storeDataPublishService, ProcessorMapperService processorMapperService) {
        return new CustomerDataReadyProcessor(objectMapper, queueProperties, storeDataPublishService, processorMapperService);
    }

    private UsageDataReadyProcessor createUsageDataReadyProcessor(ObjectMapper objectMapper,
            QueueProperties queueProperties,
            StoreDataPublishService storeDataPublishService, ProcessorMapperService processorMapperService) {
        return new UsageDataReadyProcessor(objectMapper, queueProperties, storeDataPublishService, processorMapperService);
    }
}
