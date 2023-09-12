package com.consensus.gtvadapter.gateway.client;


import com.consensus.common.web.DownstreamWebClient;
import com.consensus.gtvadapter.common.models.event.gtv.request.BaseGtvRequest;
import com.consensus.gtvadapter.common.models.event.gtv.response.GtvResponseData;
import com.consensus.gtvadapter.config.properties.GtvProperties;
import com.consensus.gtvadapter.util.GtvConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class GtvRestClient {

    @Qualifier("gtvWebClient")
    private final DownstreamWebClient gtvWebClient;
    private final GtvProperties gtvProperties;

    public GtvResponseData executeGtvRequest(BaseGtvRequest<?> gtvRequest) {
        GtvResponseData gtvResponseData = new GtvResponseData();
        try {
            gtvResponseData.setRequestDateTime(Instant.now());

            ResponseEntity<String> response = gtvWebClient.getWebClient()
                    .method(gtvRequest.getMethod())
                    .uri(gtvRequest.getApi())
                    .bodyValue(gtvRequest.getBody())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(GtvConstants.HttpHeaders.GTV_API_KEY, gtvProperties.getApiXKey())
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse -> Mono.empty())
                    .toEntity(String.class)
                    .block();

            // It will never happen as WebClient never returns 'null' as response entity
            if (response == null) {
                throw new IllegalArgumentException("WebClient returned nullable response entity.");
            }

            gtvResponseData.setResponseDateTime(Instant.now());
            gtvResponseData.setStatusCode(response.getStatusCode().value());
            gtvResponseData.setPayload(response.getBody());
        } catch (WebClientRequestException wcrEx) {
            log.error("Web client error - Method[{}] URI[{}]: {}", wcrEx.getMethod(), wcrEx.getUri(), wcrEx.getMessage());
            gtvResponseData.setResponseDateTime(Instant.now());
            gtvResponseData.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
        } catch (Exception ex) {
            log.error("Exception executing GTV request: {}", ex.getMessage(), ex);
        }

        long executionTime = gtvResponseData.getResponseDateTime().toEpochMilli() - gtvResponseData.getRequestDateTime().toEpochMilli();
        log.info("GTV Request - Method:[{}] API:[{}] Status:[{}] Execution Time:[{}ms]",
                gtvRequest.getMethod(), gtvRequest.getApi(), gtvResponseData.getStatusCode(), executionTime);

        return gtvResponseData;
    }
}
