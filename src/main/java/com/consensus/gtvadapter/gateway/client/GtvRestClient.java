package com.consensus.gtvadapter.gateway.client;


import com.consensus.common.web.DownstreamWebClient;
import com.consensus.gtvadapter.common.models.event.GtvAccountCreationEvent;
import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import com.consensus.gtvadapter.config.properties.GtvProperties;
import com.consensus.gtvadapter.util.GtvConstants;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class GtvRestClient {

    @Qualifier("gtvWebClient")
    private final DownstreamWebClient gtvWebClient;
    private final GtvProperties gtvProperties;

    public GtvRequestDetails createAccount(GtvAccountCreationEvent accountCreationEvent){
        GtvRequestDetails gtvRequestDetails = new GtvRequestDetails();
        try {
            gtvRequestDetails.setRequestDateTime(Instant.now());

            ResponseEntity<JsonNode> response = gtvWebClient.getWebClient()
                    .method(HttpMethod.POST)
                    .uri(accountCreationEvent.getApi())
                    .bodyValue(accountCreationEvent.getBody())
                    .accept(MediaType.APPLICATION_JSON)
                    .header(GtvConstants.HttpHeaders.GTV_API_KEY, gtvProperties.getApiXKey())
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse -> Mono.empty())
                    .toEntity(JsonNode.class)
                    //.retryWhen(gtvRetry) Retries can be added automatically
                    .block();

            gtvRequestDetails.setResponseDateTime(Instant.now());
            gtvRequestDetails.setStatusCode(response.getStatusCode().value());
            gtvRequestDetails.setPayload(response.getBody());
            long executionTime = gtvRequestDetails.getResponseDateTime().toEpochMilli() - gtvRequestDetails.getRequestDateTime().toEpochMilli();

            log.info("GTV Request - Method:[{}] API:[{}] Status:[{}] Execution Time:[{}ms]", accountCreationEvent.getMethod(), accountCreationEvent.getApi(), gtvRequestDetails.getStatusCode(), executionTime);
        } catch (Exception ex) {
            log.error("GTV Account creation failed {}", ex.getMessage());
        }
        return gtvRequestDetails;
    }
}
