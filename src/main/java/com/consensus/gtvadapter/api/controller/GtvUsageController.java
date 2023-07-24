package com.consensus.gtvadapter.api.controller;

import com.consensus.gtvadapter.api.models.request.UsageEventsBulkRequest;
import com.consensus.gtvadapter.api.models.response.UsageEventsBulkResponse;
import com.consensus.gtvadapter.api.service.GtvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(GtvUsageController.USAGE_BASE_URL)
public class GtvUsageController {

    public static final String USAGE_BASE_URL = "/billing";

    private final GtvService gtvService;

    @PostMapping("/{account}/usage-events/bulk")
    public ResponseEntity<UsageEventsBulkResponse> createBillingUsages(@RequestBody UsageEventsBulkRequest usageEventsBulkRequest){

        final Optional<UsageEventsBulkResponse> optionalUsageEventsBulkResponse = gtvService.createUsageBulk(usageEventsBulkRequest);
        final UsageEventsBulkResponse response = optionalUsageEventsBulkResponse.orElseThrow(IllegalStateException::new);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
