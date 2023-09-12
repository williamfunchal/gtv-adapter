package com.consensus.gtvadapter.gateway.service;

import com.consensus.gtvadapter.common.models.event.gtv.request.BaseGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.response.BaseGtvResponse;
import com.consensus.gtvadapter.common.models.gtv.GtvData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class GatewayEventProcessingService {

    private final Map<String, GatewayEventProcessor<? extends BaseGtvRequest<? extends GtvData>>> eventProcessors;

    public GatewayEventProcessingService(List<GatewayEventProcessor<? extends BaseGtvRequest<? extends GtvData>>> eventProcessors) {
        this.eventProcessors = eventProcessors.stream()
                .collect(Collectors.toMap(
                        GatewayEventProcessor::eventType,
                        Function.identity(),
                        (o1, o2) -> o1)
                );
    }

    public BaseGtvResponse<?> processEvent(BaseGtvRequest<? extends GtvData> gtvRequest){
        String eventType = gtvRequest.getEventType();
        @SuppressWarnings("unchecked")
        GatewayEventProcessor<BaseGtvRequest<? extends GtvData>> eventProcessor = (GatewayEventProcessor<BaseGtvRequest<? extends GtvData>>) eventProcessors.get(eventType);
        if (isNull(eventProcessor)) {
            throw new IllegalArgumentException("Unable to process SQS event of unknown type: " + eventType);
        }
        return eventProcessor.process(gtvRequest);
    }
}
