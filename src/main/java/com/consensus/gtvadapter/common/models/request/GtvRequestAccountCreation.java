package com.consensus.gtvadapter.common.models.request;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.models.rawdata.IspCustomerData;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeName("account-creation")
public class GtvRequestAccountCreation extends GtvRequest{
    private AccountCreationRequestBody body;
    private IspCustomerData ispData;
}
