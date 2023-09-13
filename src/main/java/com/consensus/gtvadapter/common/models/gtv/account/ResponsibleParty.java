package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponsibleParty {

    private PartyType partyType;
    private String externalCustomerNum;
    private String organizationName;
    private List<? extends AddressBase> addresses;
}
