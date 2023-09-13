package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostalAddress extends AddressBase {

    public static final String BILLING_PURPOSE = "BILLING";

    private String country;
    private String line1;
    private String line2;
    private String city;
    private String regionOrState;
    private String postalCode;

    public PostalAddress() {
        this.addressType = AddressType.POSTAL;
        this.purpose = BILLING_PURPOSE;
    }
}

