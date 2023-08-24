package com.consensus.gtvadapter.util;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;

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

    public static Map<String, MessageAttributeValue> createMessageAttributesWithCorrelationId(String correlationId){
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        final MessageAttributeValue correlationIdAttribute = createAttribute(correlationId);
        attributes.put(CORRELATION_ID, correlationIdAttribute);
        return attributes;
    }
}
