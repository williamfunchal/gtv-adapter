package com.consensus.gtvadapter.repository.storage;

import com.consensus.gtvadapter.repository.entities.UsageDbEvent;
import org.springframework.data.repository.Repository;

public interface UsageEventsRepository extends Repository<UsageDbEvent, String>, UsageEventsRepositoryCustom {

    UsageDbEvent findByEventId(String eventId);

    void save(UsageDbEvent usageDbEvent);
}
