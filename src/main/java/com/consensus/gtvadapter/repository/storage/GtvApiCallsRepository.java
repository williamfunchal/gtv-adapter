package com.consensus.gtvadapter.repository.storage;

import com.consensus.gtvadapter.repository.entities.GtvApiCall;
import org.springframework.data.repository.Repository;

public interface GtvApiCallsRepository extends Repository<GtvApiCall, String> {

    GtvApiCall findByCorrelationId(String eventId);

    void save(GtvApiCall gtvApiCall);
}
