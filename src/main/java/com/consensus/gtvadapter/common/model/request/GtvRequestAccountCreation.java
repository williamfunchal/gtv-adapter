package com.consensus.gtvadapter.common.model.request;

import com.consensus.gtvadapter.common.model.gtv.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.model.rawdata.IspCustumerData;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeName("account creation")
public class GtvRequestAccountCreation extends GtvRequest{
    private AccountCreationRequestBody body;
    private IspCustumerData ispData;
}
