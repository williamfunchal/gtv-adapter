package com.consensus.gtvadapter.processor.mapper;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.common.models.MappedData;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.DataMappingStoreEvent;
import com.consensus.gtvadapter.common.models.event.IspNewCustomerEvent;
import com.consensus.gtvadapter.common.models.rawdata.IspRawDataCustomer;
import com.consensus.gtvadapter.common.models.request.GtvRequestAccountCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessorMapper {

    private final AccountMapper accountMapper;

    public static final String ACCOUNT_CREATE_API = "/billing/2/billing-accounts";

    public AdapterEvent mapGtvRequest(AdapterEvent adapterEvent){

        if(adapterEvent instanceof IspNewCustomerEvent){
            final IspNewCustomerEvent ispNewCustomerEvent = (IspNewCustomerEvent) adapterEvent;

            final IspRawDataCustomer ispRawDataCustomer = new IspRawDataCustomer();
            ispRawDataCustomer.setOperation(ispNewCustomerEvent.getOperation());
            ispRawDataCustomer.setTableName(ispNewCustomerEvent.getTableName());
            ispRawDataCustomer.setData(ispNewCustomerEvent.getData());

            final GtvRequestAccountCreation gtvRequestAccountCreation = new GtvRequestAccountCreation();
            gtvRequestAccountCreation.setApi(ACCOUNT_CREATE_API);
            gtvRequestAccountCreation.setMethod(HttpMethod.POST);
            gtvRequestAccountCreation.setIspData(ispRawDataCustomer.getData());
            gtvRequestAccountCreation.setBody(accountMapper.toAccountCreationRequestBody(ispRawDataCustomer.getData()));

            final MappedData mappedData = new MappedData();
            mappedData.setRequests(List.of(gtvRequestAccountCreation));

            final IspGtvMapping ispGtvMapping = new IspGtvMapping();
            ispGtvMapping.setMappedData(mappedData);
            ispGtvMapping.setRawData(ispRawDataCustomer);

            final DataMappingStoreEvent dataMappingStoreEvent = new DataMappingStoreEvent();
            dataMappingStoreEvent.setIspGtvMapping(ispGtvMapping);
            dataMappingStoreEvent.setCorrelationId(ispNewCustomerEvent.getCorrelationId());

            return dataMappingStoreEvent;
        }

        return null;
    }
}
