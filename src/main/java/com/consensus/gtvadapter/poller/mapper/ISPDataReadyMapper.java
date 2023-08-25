package com.consensus.gtvadapter.poller.mapper;

import com.consensus.common.util.CCSIUUIDUtils;
import com.consensus.gtvadapter.common.models.dto.customer.IspS3CustomerDTO;
import com.consensus.gtvadapter.common.models.event.IspNewCustomerEvent;
import com.consensus.gtvadapter.common.models.rawdata.DataOperation;
import com.consensus.gtvadapter.common.models.rawdata.IspCustomerData;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ISPDataReadyMapper {

    public IspNewCustomerEvent map(IspS3CustomerDTO ispS3CustomerDTO){
        String correlationId = CCSIUUIDUtils.generateUUID();
        final IspNewCustomerEvent ispNewCustomerEvent = new IspNewCustomerEvent();
        ispNewCustomerEvent.setCorrelationId(UUID.fromString(correlationId));
        ispNewCustomerEvent.setTableName("ISPCUSTOMER");
        ispNewCustomerEvent.setOperation(DataOperation.get(ispS3CustomerDTO.getOp()));
        ispNewCustomerEvent.setData(IspCustomerData.builder()
                .customerkey(ispS3CustomerDTO.getCustomerkey())
                .company(ispS3CustomerDTO.getCompany())
                .country(ispS3CustomerDTO.getCountry())
                .addressLine1(ispS3CustomerDTO.getAddressLine1())
                .addressLine2(ispS3CustomerDTO.getAddressLine2())
                .city(ispS3CustomerDTO.getCity())
                .mailRegion(ispS3CustomerDTO.getMailRegion())
                .mailCode(ispS3CustomerDTO.getMailCode())
                .emailAddress(ispS3CustomerDTO.getEmailAddress())
                .startDate(ispS3CustomerDTO.getStartDate())
                .currencyCode(ispS3CustomerDTO.getCurrencyCode())
                //.paymentTerms(ispS3CustomerDTO.getPaymentTerms()) TODO - check if this is needed
                .resellerId(ispS3CustomerDTO.getResellerId())
                .offerCode(ispS3CustomerDTO.getOfferCode())
                .build());

        return ispNewCustomerEvent;
    }
}
