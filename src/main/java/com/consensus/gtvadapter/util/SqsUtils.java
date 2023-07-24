package com.consensus.gtvadapter.util;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

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
}
