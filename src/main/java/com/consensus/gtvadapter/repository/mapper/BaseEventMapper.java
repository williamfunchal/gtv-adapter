package com.consensus.gtvadapter.repository.mapper;

import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.util.CollectionUtils.isEmpty;

public abstract class BaseEventMapper {

    private static final TypeReference<Map<String, Object>> DATA_TYPE_REFERENCE = new TypeReference<>() { };

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Named("convertToAttributeValue")
    protected AttributeValue convertToAttributeValue(Object data) {
        Map<String, Object> objectMap = objectMapper.convertValue(data, DATA_TYPE_REFERENCE);
        if (isEmpty(objectMap)) {
            return new AttributeValue().withM(emptyMap());
        }
        return ItemUtils.toAttributeValue(objectMap);
    }
}
