package com.consensus.gtvadapter.processor.mapper;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.common.models.MappedData;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.DataMappingStoreEvent;
import com.consensus.gtvadapter.common.models.event.IspNewCustomerEvent;
import com.consensus.gtvadapter.common.models.rawdata.IspRawDataCustomer;
import com.consensus.gtvadapter.common.models.request.GtvRequestAccountCreation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class IspNewCustomerEventMapper implements ProcessorMapper<IspNewCustomerEvent> {

    private static final String ACCOUNT_CREATE_API = "/billing/2/billing-accounts";

    private final AccountMapper accountMapper;

    @Override
    public String eventType() {
        return IspNewCustomerEvent.TYPE;
    }

    @Override
    public AdapterEvent process(IspNewCustomerEvent event) {
        IspRawDataCustomer ispRawDataCustomer = new IspRawDataCustomer();
        ispRawDataCustomer.setOperation(event.getOperation());
        ispRawDataCustomer.setTableName(event.getTableName());
        ispRawDataCustomer.setData(event.getData());

        GtvRequestAccountCreation gtvRequestAccountCreation = new GtvRequestAccountCreation();
        gtvRequestAccountCreation.setApi(ACCOUNT_CREATE_API);
        gtvRequestAccountCreation.setMethod(HttpMethod.POST);
        gtvRequestAccountCreation.setBody(accountMapper.toAccountCreationRequestBody(ispRawDataCustomer.getData()));

        MappedData mappedData = new MappedData();
        mappedData.setRequests(List.of(gtvRequestAccountCreation));

        IspGtvMapping ispGtvMapping = new IspGtvMapping();
        ispGtvMapping.setMappedData(mappedData);
        ispGtvMapping.setRawData(ispRawDataCustomer);

        DataMappingStoreEvent dataMappingStoreEvent = new DataMappingStoreEvent();
        dataMappingStoreEvent.setIspGtvMapping(ispGtvMapping);
        dataMappingStoreEvent.setCorrelationId(event.getCorrelationId());
        dataMappingStoreEvent.setEventId(event.getEventId());

        return dataMappingStoreEvent;
    }
}
