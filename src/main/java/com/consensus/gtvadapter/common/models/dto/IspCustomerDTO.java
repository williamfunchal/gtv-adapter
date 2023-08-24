package com.consensus.gtvadapter.common.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IspCustomerDTO {
    private String customerkey;
    private String company;
    private String country;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String mailRegion;
    private String mailCode;
    private String emailAddress;
    private String startDate;
    private String currencyCode;
    private String paymentTerms;
    private String resellerId;
    private String offerCode;
}
