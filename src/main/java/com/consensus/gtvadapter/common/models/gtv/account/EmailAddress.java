package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailAddress extends AddressBase{

    public static final String PURPOSE_PRIMARY = "PRIMARY";
    private String email;

    @Builder
    public EmailAddress(AddressType addressType, String purpose, String email) {
        super(addressType, purpose);
        this.email = email;
    }
}
