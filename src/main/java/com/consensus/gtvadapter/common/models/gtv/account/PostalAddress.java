package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostalAddress extends AddressBase{

    private String country;
    private String line1;
    private String line2;
    private String city;
    private String regionOrState;
    private String postalCode;

    @Builder
    public PostalAddress(AddressType addressType, String purpose, String country, String line1, String line2, String city, String regionOrState, String postalCode) {
        super(addressType, purpose);
        this.country = country;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.regionOrState = regionOrState;
        this.postalCode = postalCode;
    }
}

