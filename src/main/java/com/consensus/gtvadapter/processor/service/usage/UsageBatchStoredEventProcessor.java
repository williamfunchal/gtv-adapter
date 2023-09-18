package com.consensus.gtvadapter.processor.service.usage;

import com.consensus.gtvadapter.common.models.event.UsageAdapterEvent;
import com.consensus.gtvadapter.common.models.event.gtv.request.UsageCreationGtvRequest;
import com.consensus.gtvadapter.common.models.event.isp.stored.UsageBatchStoredEvent;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageEventsBulkRequest;
import com.consensus.gtvadapter.processor.service.SingleEventProcessor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
class UsageBatchStoredEventProcessor implements SingleEventProcessor<UsageBatchStoredEvent> {

    private static final String USAGE_REQUEST_MODE = "FAIL_ON_EXISTING";
    private static final String USAGE_CREATE_API = "/billing/2/usage-events/bulk";

    @Override
    public String eventType() {
        return UsageBatchStoredEvent.TYPE;
    }

    @Override
    public UsageCreationGtvRequest process(UsageBatchStoredEvent event) {
        UsageCreationGtvRequest gtvRequest = new UsageCreationGtvRequest();
        gtvRequest.setEventId(event.getEventId());
        gtvRequest.setCorrelationId(event.getCorrelationId());
        gtvRequest.setApi(USAGE_CREATE_API);
        gtvRequest.setMethod(HttpMethod.POST);
        gtvRequest.setBody(createRequestBody(event.getEventBatch()));
        return gtvRequest;
    }

    private UsageEventsBulkRequest createRequestBody(List<UsageAdapterEvent> eventBatch) {
        var body = new UsageEventsBulkRequest();
        body.setMode(USAGE_REQUEST_MODE);
        body.setUsageEvents(eventBatch.stream().map(UsageAdapterEvent::getGtvData).collect(toList()));
        return body;
    }
}
