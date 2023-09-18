package com.consensus.gtvadapter.repository.storage;

import com.consensus.gtvadapter.repository.entities.UsageDbEvent;

import java.util.List;
import java.util.Set;

public interface UsageEventsRepositoryCustom  {

    List<UsageDbEvent> findByEventIdIn(Set<String> eventIds);

    void saveAll(List<UsageDbEvent> usageDbEvents);

    void updateAll(List<UsageDbEvent> usageDbEvents);

}
