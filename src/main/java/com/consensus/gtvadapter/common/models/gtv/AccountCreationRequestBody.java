package com.consensus.gtvadapter.common.models.gtv;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreationRequestBody {
    private ResponsibleParty responsibleParty;
    private Instant startDate;
    private CurrencyCode currencyCode;
    private BillCycle billCycle;
    private BillType billType;
    private BillingAccountCategory billingAccountCategory;
    private List<CustomFieldValue> customFieldValues;
}
