package com.consensus.gtvadapter.common.models.gtv.account;

import com.consensus.gtvadapter.common.models.gtv.GtvData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreationGtvData implements GtvData {

    private ResponsibleParty responsibleParty;
    private Instant startDate;
    private CurrencyCode currencyCode;
    private BillCycle billCycle;
    private BillType billType;
    private BillingAccountCategory billingAccountCategory;
    private List<CustomFieldValue> customFieldValues;
    private PaymentTerm paymentTerm;
}
