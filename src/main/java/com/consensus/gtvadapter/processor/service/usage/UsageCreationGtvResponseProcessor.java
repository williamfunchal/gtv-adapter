package com.consensus.gtvadapter.processor.service.usage;

import com.consensus.gtvadapter.common.models.event.gtv.response.UsageCreationGtvResponse;
import com.consensus.gtvadapter.common.models.event.isp.update.UsageUpdateEvent;
import com.consensus.gtvadapter.processor.service.SingleEventProcessor;
import org.springframework.stereotype.Component;

@Component
class UsageCreationGtvResponseProcessor implements SingleEventProcessor<UsageCreationGtvResponse> {

    @Override
    public String eventType() {
        return UsageCreationGtvResponse.TYPE;
    }

    @Override
    public UsageUpdateEvent process(UsageCreationGtvResponse responseEvent) {
        UsageUpdateEvent updateEvent = new UsageUpdateEvent();
        updateEvent.setEventId(responseEvent.getEventId());
        updateEvent.setCorrelationId(responseEvent.getCorrelationId());
        updateEvent.setApi(responseEvent.getApi());
        updateEvent.setMethod(responseEvent.getMethod());
        updateEvent.setBody(responseEvent.getBody());
        updateEvent.setResult(responseEvent.getResult());
        return updateEvent;
    }
}
