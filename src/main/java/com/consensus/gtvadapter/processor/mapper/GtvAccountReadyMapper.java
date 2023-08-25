package com.consensus.gtvadapter.processor.mapper;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.common.models.MappedData;
import com.consensus.gtvadapter.common.models.event.DataMappingStoredEvent;
import com.consensus.gtvadapter.common.models.event.GtvAccountCreationEvent;
import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.models.gtv.account.PartyType;
import com.consensus.gtvadapter.common.models.gtv.account.ResponsibleParty;
import com.consensus.gtvadapter.common.models.request.GtvRequest;
import com.consensus.gtvadapter.common.models.request.GtvRequestAccountCreation;
import com.consensus.gtvadapter.util.GtvConstants;

@Component
public class GtvAccountReadyMapper {
    public GtvAccountCreationEvent map(DataMappingStoredEvent dataMappingStoredEvent) {
        IspGtvMapping ispGtvMapping = dataMappingStoredEvent.getIspGtvMapping();
        MappedData mappedData = ispGtvMapping.getMappedData();
        Integer lastPosition = mappedData.getRequests().size() - 1;
        GtvRequest gtvRequest = mappedData.getRequests().get(lastPosition);
        GtvRequestAccountCreation gtvRequestAccountCreation = (GtvRequestAccountCreation) gtvRequest;
        AccountCreationRequestBody accountCreationRequestBody = gtvRequestAccountCreation.getBody();

        GtvAccountCreationEvent gtvAccountCreationEvent = GtvAccountCreationEvent.builder()
            .correlationId(dataMappingStoredEvent.getCorrelationId())
            .method(HttpMethod.POST)
            .api(GtvConstants.ACCOUNT_CREATE_API)
            .body(AccountCreationRequestBody.builder()
                .responsibleParty(ResponsibleParty.builder()
                    .partyType(PartyType.ORGANIZATION)
                    .externalCustomerNum("7777")
                    .organizationName(accountCreationRequestBody.getResponsibleParty().getOrganizationName())
                    .addresses(accountCreationRequestBody.getResponsibleParty().getAddresses())
                    .build())
                .startDate(accountCreationRequestBody.getStartDate())
                .currencyCode(accountCreationRequestBody.getCurrencyCode())
                .billCycle(accountCreationRequestBody.getBillCycle())
                .billType(accountCreationRequestBody.getBillType())
                .billingAccountCategory(accountCreationRequestBody.getBillingAccountCategory())
                .customFieldValues(accountCreationRequestBody.getCustomFieldValues())
                .build())
            .build();
        return gtvAccountCreationEvent;
    }
}
