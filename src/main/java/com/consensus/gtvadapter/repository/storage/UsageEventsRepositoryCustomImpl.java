package com.consensus.gtvadapter.repository.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.consensus.gtvadapter.repository.entities.UsageDbEvent;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class UsageEventsRepositoryCustomImpl implements UsageEventsRepositoryCustom {

    private final DynamoDBMapper dynamoDBMapper;

    public UsageEventsRepositoryCustomImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public List<UsageDbEvent> findByEventIdIn(Set<String> eventIds) {
        List<UsageDbEvent> usageQuery = eventIds.stream().map(this::createUsageDbEventQuery).collect(toList());
        return dynamoDBMapper.batchLoad(usageQuery).values()
                .stream()
                .flatMap(List::stream)
                .map(obj -> (UsageDbEvent) obj)
                .collect(toList());
    }

    @Override
    public void saveAll(List<UsageDbEvent> usageDbEvents) {
        TransactionWriteRequest txRequest = new TransactionWriteRequest();
        usageDbEvents.forEach(txRequest::addPut);
        dynamoDBMapper.transactionWrite(txRequest);
    }

    @Override
    public void updateAll(List<UsageDbEvent> usageDbEvents) {
        TransactionWriteRequest txRequest = new TransactionWriteRequest();
        usageDbEvents.forEach(txRequest::addUpdate);
        dynamoDBMapper.transactionWrite(txRequest);
    }

    private UsageDbEvent createUsageDbEventQuery(String eventId) {
        return UsageDbEvent.builder()
                .eventId(eventId)
                .build();
    }
}
