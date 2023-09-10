package com.consensus.gtvadapter.common.sqs.consumer;

import com.amazonaws.services.sqs.model.Message;
import com.consensus.common.sqs.CCSIQueueMessage;
import com.consensus.common.sqs.CCSIQueueMessageContext;
import com.consensus.common.util.CCSIUUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.*;
import static com.consensus.common.sqs.CCSIQueueTraceContextHolder.RECEIVE_COUNT;

@Slf4j
@Component
public class QueueMessageContextCreator {

    public CCSIQueueMessageContext createMessageContext(Message message) {
        CCSIQueueMessage queueMessage = buildMessage(message);
        return CCSIQueueMessageContext
                .builder()
                .message(queueMessage)
                .approximateReceiveCount(extractAttribute(queueMessage, RECEIVE_COUNT))
                .correlationId(extractCorrelationId(queueMessage))
                .eventType(extractMessageData(queueMessage, EVENT_TYPE))
                .build();
    }

    private CCSIQueueMessage buildMessage(Message message) {
        return CCSIQueueMessage
                .builder()
                .attributes(message.getAttributes())
                .messageAttributes(convertMessageAttributes(message))
                .body(message.getBody())
                .messageId(message.getMessageId())
                .receiptHandle(message.getReceiptHandle())
                .build();
    }

    private Map<String, String> convertMessageAttributes(Message message) {
        return message.getMessageAttributes()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getStringValue()));
    }

    private String extractCorrelationId(CCSIQueueMessage message) {
        return Optional.ofNullable(extractMessageData(message, CORRELATION_ID))
                .or(() -> Optional.ofNullable(extractMessageData(message, LEGACY_CORRELATION_ID)))
                .orElseGet(CCSIUUIDUtils::generateUUID);
    }

    private String extractAttribute(CCSIQueueMessage message, String attrName) {
        return Optional.ofNullable(message.getAttributes()).map(attrs -> attrs.get(attrName)).orElse(null);
    }

    private String extractMessageData(CCSIQueueMessage message, String attrName) {
        return Optional.ofNullable(extractMessageAttribute(message, attrName))
                .orElseGet(() -> extractMessageFieldFromRoot(message, attrName));
    }

    private String extractMessageAttribute(CCSIQueueMessage message, String attrName) {
        return Optional.ofNullable(message.getMessageAttributes())
                .map(attrs -> attrs.get(attrName))
                .orElse(null);
    }

    private String extractMessageFieldFromRoot(CCSIQueueMessage message, String attrName) {
        try {
            JSONObject json = new JSONObject(message.getBody());
            return json.getString(attrName);
        } catch (Exception e) {
            log.debug("Not able to extract '{}' from SQS message body", attrName, e);
            return null;
        }
    }
}
