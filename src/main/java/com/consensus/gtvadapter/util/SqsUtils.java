package com.consensus.gtvadapter.util;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.consensus.gtvadapter.util.GtvConstants.SqsMessageAttributes.*;
import static org.apache.commons.lang3.StringUtils.endsWithIgnoreCase;

@UtilityClass
public class SqsUtils {

    private static final String FIFO_QUEUE_SUFFIX = ".fifo";

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

    public static Map<String, MessageAttributeValue> createMessageAttributes(AdapterEvent sqsEvent) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put(EVENT_ID, createAttribute(sqsEvent.getEventId()));
        attributes.put(EVENT_TYPE, createAttribute(sqsEvent.getEventType()));
        attributes.put(CORRELATION_ID, createAttribute(sqsEvent.getCorrelationId()));
        return attributes;
    }

    public static boolean isFifo(String queueUrl) {
        return endsWithIgnoreCase(queueUrl, FIFO_QUEUE_SUFFIX);
    }
}
