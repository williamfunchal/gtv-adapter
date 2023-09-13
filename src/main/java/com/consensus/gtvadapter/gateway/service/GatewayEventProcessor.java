package com.consensus.gtvadapter.gateway.service;

import com.consensus.gtvadapter.common.models.event.gtv.request.BaseGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.response.BaseGtvResponse;
import com.consensus.gtvadapter.common.models.gtv.GtvData;

interface GatewayEventProcessor<T extends BaseGtvRequest<? extends GtvData>> {

    String eventType();

    BaseGtvResponse<? extends GtvData> process(T message);
}
