package com.consensus.gtvadapter.repository.storage;

import com.consensus.gtvadapter.repository.entities.CustomerDbEvent;
import org.springframework.data.repository.Repository;

public interface CustomerEventsRepository extends Repository<CustomerDbEvent, String> {

    CustomerDbEvent findByEventId(String eventId);

    void save(CustomerDbEvent customerDbEvent);
}
