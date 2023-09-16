package com.consensus.gtvadapter.processor.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.sqs.CCSIQueueMessage;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.sqs.CCSIQueueMessageResult;
import com.consensus.common.sqs.CCSIQueueMessageStatus;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.isp.store.UsageBatchStoreEvent;
import com.consensus.gtvadapter.common.sqs.listener.QueueMessageBatchProcessor;
import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.processor.service.EventProcessingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.consensus.gtvadapter.util.GtvConstants.SqsMessageAttributes.EVENT_TYPE;

@Slf4j
@Component
public class IspDataReadyProcessor extends BaseDataReadyProcessor implements QueueMessageBatchProcessor {

    private static final Set<String> MESSAGE_BATCH_GROUPS = Set.of("usage-isp-new");

    public IspDataReadyProcessor(ObjectMapper objectMapper, QueueProperties queueProperties,
            DataReadyToStorePublishService dataReadyToStorePublishService,
            EventProcessingService eventProcessingService) {
        super(objectMapper, queueProperties, dataReadyToStorePublishService, eventProcessingService);
    }

    @Override
    public CCSIQueueMessageResult process(String groupName, List<CCSIQueueMessageContext> messages) {
        log.debug("Processing usage events batch: {}", messages.size());

        List<AdapterEvent> adapterEvents = new ArrayList<>();

        for(CCSIQueueMessageContext messageContext: messages){
            try{
                adapterEvents.add(parseMessage(messageContext.getMessage()));
            }catch (JsonProcessingException jpEx){
                log.error("Exception parsing SQS event: {}", jpEx.getMessage(), jpEx);
                return CCSIQueueMessageResult.builder()
                        .logMessage("Message body parsing failed")
                        .status(CCSIQueueMessageStatus.NOOP)
                        .build();
            }
        }

        AdapterEvent processedEvent;

        try {
            processedEvent = eventProcessingService.processEvent(adapterEvents);
        } catch (DateTimeException dtEx) {
            log.error("Exception parsing date from SQS event: {}", dtEx.getMessage(), dtEx);
            return CCSIQueueMessageResult.builder()
                    .logMessage("Date parsing failed: " + dtEx.getMessage())
                    .status(CCSIQueueMessageStatus.NOOP)
                    .build();
        } catch (Exception ex) {
            log.error("Exception processing SQS event: {}", ex.getMessage(), ex);
            return CCSIQueueMessageResult.builder()
                    .logMessage("GTV request mapping failed")
                    .status(CCSIQueueMessageStatus.RECOVERABLE_ERROR)
                    .build();
        }

        if(processedEvent instanceof UsageBatchStoreEvent) {
            dataReadyToStorePublishService.publishMessage(processedEvent);
        }

        return CCSIQueueMessageResult.builder()
                .status(CCSIQueueMessageStatus.SUCCESS)
                .build();
    }

    @Override
    public Set<String> getBatchGroups() {
        return MESSAGE_BATCH_GROUPS;
    }

    @Override
    public String messageGroupId(Message message) {
        return Optional.ofNullable(message.getMessageAttributes())
                .map(attrs -> attrs.get(EVENT_TYPE))
                .map(MessageAttributeValue::getStringValue)
                .filter(MESSAGE_BATCH_GROUPS::contains)
                .orElseGet(() -> super.messageGroupId(message));
    }

    private AdapterEvent parseMessage(CCSIQueueMessage message) throws JsonProcessingException {
        return objectMapper.readValue(message.getBody(), AdapterEvent.class);
    }


}
