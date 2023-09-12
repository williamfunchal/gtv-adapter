package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmailAddress extends AddressBase{

    public static final String PURPOSE_PRIMARY = "PRIMARY";

    private String email;

    public EmailAddress() {
        this.purpose = PURPOSE_PRIMARY;
        this.addressType = AddressType.EMAIL;
    }
}
