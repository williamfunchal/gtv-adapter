package com.consensus.gtvadapter.gateway.client;


import com.consensus.gtvadapter.common.models.event.GtvAccountCreationEvent;
import com.consensus.gtvadapter.config.GtvProperties;
import com.consensus.gtvadapter.common.models.request.GtvRequestDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class GtvRestClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final GtvProperties gtvProperties;

    public GtvRequestDetails createAccount(GtvAccountCreationEvent accountCreationEvent){
        final GtvRequestDetails gtvRequestDetails = new GtvRequestDetails();
        final String url = gtvProperties.getHost() + accountCreationEvent.getApi();
        try {
            HttpEntity<String> request =
                    new HttpEntity<>(objectMapper.writeValueAsString(accountCreationEvent.getBody()), getHeaders());

            gtvRequestDetails.setRequestDateTime(Instant.now());
            final ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
            gtvRequestDetails.setResponseDateTime(Instant.now());
            gtvRequestDetails.setStatusCode(response.getStatusCode().value());
            gtvRequestDetails.setPayload(response.getBody());
            final long executionTime = gtvRequestDetails.getResponseDateTime().toEpochMilli() - gtvRequestDetails.getRequestDateTime().toEpochMilli();

            log.info("GTV Request - Method:[{}] API:[{}] Status:[{}] Execution Time:[{}ms]", accountCreationEvent.getMethod(), accountCreationEvent.getApi(), gtvRequestDetails.getStatusCode(), executionTime);
        } catch (Exception ex) {
            log.error("GTV Account creation failed {}", ex.getMessage());
        }
        return gtvRequestDetails;
    }

    private HttpHeaders getHeaders(){
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Api-Key", gtvProperties.getApiXKey());
        return headers;
    }
}
