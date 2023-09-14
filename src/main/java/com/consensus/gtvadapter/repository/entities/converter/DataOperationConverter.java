package com.consensus.gtvadapter.repository.entities.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.consensus.gtvadapter.common.models.rawdata.DataOperation;

import java.util.Optional;

public class DataOperationConverter implements DynamoDBTypeConverter<String, DataOperation> {

    @Override
    public String convert(DataOperation status) {
        return Optional.ofNullable(status).map(DataOperation::value).orElse(null);
    }

    @Override
    public DataOperation unconvert(String str) {
        return Optional.ofNullable(str).map(DataOperation::fromValue).orElse(null);
    }
}
