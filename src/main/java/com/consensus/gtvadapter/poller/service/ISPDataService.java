package com.consensus.gtvadapter.poller.service;


import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtvadapter.poller.sqs.ISPDataPublishService;
import com.consensus.gtvadapter.util.SqsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ISPDataService {

    final ISPDataPublishService ispDataPublishService;

    @Scheduled(fixedDelay = 10000)
    public void fetchISPData(){
        log.info("New ISP data found");
        ispDataPublishService.publishMessageToQueue("New data event", getMessageAttributes());
    }

    private Map<String, MessageAttributeValue> getMessageAttributes(){
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        final MessageAttributeValue correlationIdAttribute = SqsUtils.createAttribute(CCSIUUIDUtils.generateUUID());
        attributes.put(CORRELATION_ID, correlationIdAttribute);
        return attributes;
    }
}
