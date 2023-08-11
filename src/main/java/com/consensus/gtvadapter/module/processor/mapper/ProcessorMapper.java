package com.consensus.gtvadapter.module.processor.mapper;

import com.consensus.gtvadapter.common.model.rawdata.DataOperation;
import com.consensus.gtvadapter.common.model.request.GtvRequest;
import com.consensus.gtvadapter.common.model.request.GtvRequestAccountCreation;
import com.consensus.gtvadapter.common.model.rawdata.IspCustumerData;
import com.consensus.gtvadapter.common.model.rawdata.IspRawData;
import com.consensus.gtvadapter.common.model.rawdata.IspRawDataCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessorMapper {

    private final AccountMapper accountMapper;

    public static final String ACCOUNT_CREATE_API = "/billing/2/billing-accounts";

    public List<GtvRequest> mapGtvRequest(IspRawData ispRawData){
        final List<GtvRequest> gtvRequests = new ArrayList<>();

        if(ispRawData instanceof IspRawDataCustomer && DataOperation.CREATE.equals(ispRawData.getOperation())){
            IspRawDataCustomer ispRawDataCustomer = (IspRawDataCustomer) ispRawData;
            final IspCustumerData ispCustumerData = ispRawDataCustomer.getData();
            final GtvRequestAccountCreation gtvRequestAccountCreation = new GtvRequestAccountCreation();
            gtvRequestAccountCreation.setApi(ACCOUNT_CREATE_API);
            gtvRequestAccountCreation.setMethod(HttpMethod.POST);
            gtvRequestAccountCreation.setIspData(ispCustumerData);
            gtvRequestAccountCreation.setBody(accountMapper.toAccountCreationRequestBody(ispCustumerData));
            gtvRequests.add(gtvRequestAccountCreation);
        }

        return gtvRequests;
    }
}
