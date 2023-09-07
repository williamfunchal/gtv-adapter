package com.consensus.gtvadapter.util;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.sqs.CCSIQueueConstants;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.consensus.gtvadapter.util.GtvConstants.SqsMessageAttributes.*;

@UtilityClass
public class SqsUtils {

    public static MessageAttributeValue createAttribute(Object value) {
        return new MessageAttributeValue()
                .withDataType("String")
                .withStringValue(getValueString(value));
    }

    private static String getValueString(Object value) {
        return Optional.ofNullable(value)
                .map(Objects::toString)
                .filter(StringUtils::hasText).orElse("null");
    }

    public static Map<String, MessageAttributeValue> createMessageAttributesWithCorrelationId(String correlationId) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        final MessageAttributeValue correlationIdAttribute = createAttribute(correlationId);
        attributes.put(CCSIQueueConstants.MessageAttributes.CORRELATION_ID, correlationIdAttribute);
        return attributes;
    }

    public static Map<String, MessageAttributeValue> createMessageAttributes(AdapterEvent sqsEvent) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put(EVENT_ID, createAttribute(sqsEvent.getEventId()));
        attributes.put(EVENT_TYPE, createAttribute(sqsEvent.getEventType()));
        attributes.put(CORRELATION_ID, createAttribute(sqsEvent.getCorrelationId()));
        return attributes;
    }
}
