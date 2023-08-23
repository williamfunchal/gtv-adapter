package com.consensus.gtvadapter.poller.service;


import static com.consensus.common.sqs.CCSIQueueConstants.MessageAttributes.CORRELATION_ID;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtvadapter.common.models.dto.customer.IspS3CustomerDTO;
import com.consensus.gtvadapter.common.models.rawdata.IspRawDataCustomer;
import com.consensus.gtvadapter.poller.mapper.ISPDataReadyMapper;
import com.consensus.gtvadapter.poller.sqs.ISPDataPublishService;
import com.consensus.gtvadapter.util.SqsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ISPDataService {

    private final ISPDataPublishService ispDataPublishService;
    private final S3ReaderService<IspS3CustomerDTO> s3ReaderService;
    private final ISPDataReadyMapper ispDataReadyMapper;
    private ObjectMapper objectMapper;

    @Scheduled(fixedDelayString = "${app.scheduler.isp-data-delay}")
    public void fetchISPData() throws IOException, URISyntaxException{
        log.info("New ISP data found");
        
        //Read CSV Customer content from S3
        List<IspS3CustomerDTO> ispS3CustomerDTO = s3ReaderService.readCsvFromS3(IspS3CustomerDTO.class);

         for(IspS3CustomerDTO customerDTO : ispS3CustomerDTO){
            log.info("CustomerDTO: {}", customerDTO);
            IspRawDataCustomer ispRawDataCustomer = ispDataReadyMapper.map(customerDTO);
            String message = objectMapper.writeValueAsString(ispRawDataCustomer);
            ispDataPublishService.publishMessageToQueue(message, getMessageAttributes());
        }             
    }

    private Map<String, MessageAttributeValue> getMessageAttributes(){
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        final MessageAttributeValue correlationIdAttribute = SqsUtils.createAttribute(CCSIUUIDUtils.generateUUID());
        attributes.put(CORRELATION_ID, correlationIdAttribute);
        return attributes;
    }
}
