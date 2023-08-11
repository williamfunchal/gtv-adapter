package com.consensus.gtvadapter.module.storage.services;

import org.bouncycastle.asn1.dvcs.Data;

import com.consensus.gtvadapter.module.storage.services.sqs.AdapterDataStoredPublishService;
import com.consensus.gtvadapter.module.storage.services.sqs.DataUpdatedPublishService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProcessorService {
    AdapterDataStoredPublishService adapterDataStoredPublishService;
    DataUpdatedPublishService dataUpdatedPublishService;      
}
