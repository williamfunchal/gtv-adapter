package com.consensus.gtvadapter.gateway.service;

import com.consensus.gtvadapter.common.models.event.gtv.request.AccountCreationGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.response.AccountCreationGtvResponse;
import com.consensus.gtvadapter.common.models.event.gtv.response.GtvResponseData;
import com.consensus.gtvadapter.gateway.client.GtvRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GtvAccountCreationEventProcessor implements GatewayEventProcessor<AccountCreationGtvRequest> {

    private final GtvRestClient gtvRestClient;

    @Override
    public String eventType() {
        return AccountCreationGtvRequest.TYPE;
    }

    @Override
    public AccountCreationGtvResponse process(AccountCreationGtvRequest event) {
        GtvResponseData responseData = gtvRestClient.executeGtvRequest(event);
        AccountCreationGtvResponse resultEvent = new AccountCreationGtvResponse();
        resultEvent.setEventId(event.getEventId());
        resultEvent.setCorrelationId(event.getCorrelationId());
        resultEvent.setApi(event.getApi());
        resultEvent.setMethod(event.getMethod());
        resultEvent.setBody(event.getBody());
        resultEvent.setResult(responseData);
        return resultEvent;
    }
}
