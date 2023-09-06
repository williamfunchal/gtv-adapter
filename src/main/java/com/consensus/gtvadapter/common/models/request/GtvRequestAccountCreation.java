package com.consensus.gtvadapter.common.models.request;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GtvRequestAccountCreation extends GtvRequest {
    private AccountCreationRequestBody body;
}
