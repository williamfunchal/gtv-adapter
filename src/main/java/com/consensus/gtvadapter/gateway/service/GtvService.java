package com.consensus.gtvadapter.gateway.service;

import com.consensus.gtvadapter.config.GtvProperties;
import com.consensus.gtvadapter.gateway.models.request.UsageEventsBulkRequest;
import com.consensus.gtvadapter.gateway.models.response.UsageEventsBulkResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GtvService {

    private static final String BILLING_USAGE_BULK_API = "/billing/2/usage-events/bulk";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final GtvProperties gtvProperties;

    public Optional<UsageEventsBulkResponse> createUsageBulk(UsageEventsBulkRequest usageEventsBulkRequest) {
        final String url = gtvProperties.getHost() + BILLING_USAGE_BULK_API;

        try {
            HttpEntity<String> request =
                    new HttpEntity<>(objectMapper.writeValueAsString(usageEventsBulkRequest), getHeaders());
            return safeGetBody(restTemplate.postForEntity(url, request, UsageEventsBulkResponse.class));
        } catch (Exception ex) {
            log.info("Something went wrong: {}", ex.getMessage());
        }

        return Optional.empty();
    }

    public Optional<UsageEventsBulkResponse> createUsageBulk(String usageEventsBulkRequest){
        final String url = gtvProperties.getHost() + BILLING_USAGE_BULK_API;

        HttpEntity<String> request = new HttpEntity<>(usageEventsBulkRequest, getHeaders());

        try {
            return safeGetBody(restTemplate.postForEntity(url, request, UsageEventsBulkResponse.class));
        } catch (Exception ex) {
            log.info("Something went wrong: {}", ex.getMessage());
        }

        return Optional.empty();
    }

    private HttpHeaders getHeaders(){
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Api-Key", gtvProperties.getApiXKey());
        return headers;
    }

    private <T> Optional<T> safeGetBody(ResponseEntity<T> response) {
        if (response.hasBody() && response.getStatusCode().is2xxSuccessful()) {
            return Optional.of(response.getBody());
        }
        return Optional.ofNullable(null);
    }
}
