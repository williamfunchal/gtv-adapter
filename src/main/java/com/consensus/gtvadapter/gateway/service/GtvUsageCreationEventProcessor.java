package com.consensus.gtvadapter.gateway.service;

import com.consensus.gtvadapter.common.models.event.gtv.request.AccountCreationGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.request.UsageCreationGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.response.AccountCreationGtvResponse;
import com.consensus.gtvadapter.common.models.event.gtv.response.GtvResponseData;
import com.consensus.gtvadapter.common.models.event.gtv.response.UsageCreationGtvResponse;
import com.consensus.gtvadapter.gateway.client.GtvRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GtvUsageCreationEventProcessor implements GatewayEventProcessor<UsageCreationGtvRequest> {

    private final GtvRestClient gtvRestClient;

    @Override
    public String eventType() {
        return UsageCreationGtvRequest.TYPE;
    }

    @Override
    public UsageCreationGtvResponse process(UsageCreationGtvRequest event) {
        GtvResponseData responseData = gtvRestClient.executeGtvRequest(event);
        UsageCreationGtvResponse resultEvent = new UsageCreationGtvResponse();
        resultEvent.setEventId(event.getEventId());
        resultEvent.setCorrelationId(event.getCorrelationId());
        resultEvent.setApi(event.getApi());
        resultEvent.setMethod(event.getMethod());
        resultEvent.setBody(event.getBody());
        resultEvent.setResult(responseData);
        return resultEvent;
    }
}
