package com.consensus.gtvadapter.processor.mapper;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.models.gtv.account.BillCycle;
import com.consensus.gtvadapter.common.models.gtv.account.BillCycleType;
import com.consensus.gtvadapter.common.models.gtv.account.BillType;
import com.consensus.gtvadapter.common.models.gtv.account.BillingAccountCategory;
import com.consensus.gtvadapter.common.models.gtv.account.CurrencyCode;
import com.consensus.gtvadapter.common.models.gtv.account.CustomField;
import com.consensus.gtvadapter.common.models.gtv.account.CustomFieldType;
import com.consensus.gtvadapter.common.models.gtv.account.CustomFieldValue;
import com.consensus.gtvadapter.common.models.gtv.account.EmailAddress;
import com.consensus.gtvadapter.common.models.gtv.account.PartyType;
import com.consensus.gtvadapter.common.models.gtv.account.PostalAddress;
import com.consensus.gtvadapter.common.models.gtv.account.ResponsibleParty;
import com.consensus.gtvadapter.common.models.rawdata.IspCustumerData;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class AccountMapper {

    public static final String ISP_DATE_PATTERN = "yyyy-MM-dd";

    //TODO CUP-68 Missing mappings
    public AccountCreationRequestBody toAccountCreationRequestBody(IspCustumerData ispCustumerData){
        final AccountCreationRequestBody accountCreationRequestBody = new AccountCreationRequestBody();
        accountCreationRequestBody.setResponsibleParty(getResponsibleParty(ispCustumerData));
        accountCreationRequestBody.setStartDate(convertToInstant(ispCustumerData.getStartDate()));
        accountCreationRequestBody.setCurrencyCode(CurrencyCode.valueOf(ispCustumerData.getCurrencyCode()));
        accountCreationRequestBody.setBillCycle(getBillCycle());
        accountCreationRequestBody.setBillType(BillType.NONE);
        accountCreationRequestBody.setBillingAccountCategory(getBillingAccountCategory());
        accountCreationRequestBody.setCustomFieldValues(getCustomFiledValues(ispCustumerData));

        return accountCreationRequestBody;
    }

    private BillCycle getBillCycle(){
        final BillCycle billCycle = new BillCycle();
        billCycle.setId("1425379");
        billCycle.setBillCycleType(BillCycleType.MONTHLY);
        return billCycle;
    }

    private BillingAccountCategory getBillingAccountCategory(){
        final BillingAccountCategory billingAccountCategory = new BillingAccountCategory();
        billingAccountCategory.setId("178");
        return billingAccountCategory;
    }

    private ResponsibleParty getResponsibleParty(IspCustumerData ispCustumerData){
        final ResponsibleParty responsibleParty = new ResponsibleParty();
        responsibleParty.setPartyType(PartyType.ORGANIZATION);
        responsibleParty.setExternalCustomerNum(ispCustumerData.getCustomerkey());
        responsibleParty.setOrganizationName(ispCustumerData.getCompany());
        final PostalAddress postalAddress = new PostalAddress();
        postalAddress.setCountry(new Locale("en", ispCustumerData.getCountry()).getISO3Country());
        postalAddress.setLine1(ispCustumerData.getAddressLine1());
        postalAddress.setLine2(ispCustumerData.getAddressLine2());
        postalAddress.setCity(ispCustumerData.getCity());
        postalAddress.setRegionOrState(ispCustumerData.getMailRegion());
        postalAddress.setPostalCode(ispCustumerData.getMailCode());
        final EmailAddress emailAddress = new EmailAddress();
        emailAddress.setEmail(ispCustumerData.getEmailAddress());
        responsibleParty.setAddresses(List.of(postalAddress, emailAddress));
        return responsibleParty;
    }

    private Instant convertToInstant(String date){
        String pattern = ISP_DATE_PATTERN;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.US);
        LocalDateTime localDateTime = LocalDate.parse(date, dateTimeFormatter).atStartOfDay();
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    private List<CustomFieldValue> getCustomFiledValues(IspCustumerData ispCustumerData){
        List<CustomFieldValue> customFieldValues = new ArrayList<>();
        customFieldValues.add(getCustomFieldValue("CCSI_corp_id", ispCustumerData.getResellerId()));
        customFieldValues.add(getCustomFieldValue("CCSI_offer_code_name", ispCustumerData.getOfferCode()));
        customFieldValues.add(getCustomFieldValue("CCSI_legacy_billing_system", ispCustumerData.getOfferCode()));
        customFieldValues.add(getCustomFieldValue("CCSI_account_start_date", Instant.now().toString()));
        customFieldValues.add(getCustomFieldValue("CCSI_min_commitment_subscription", "TBD"));
        customFieldValues.add(getCustomFieldValue("CCSI_marketplace_id", "TBD"));
        customFieldValues.add(getCustomFieldValue("CCSI_business_unit", "TBD"));
        return customFieldValues;
    }

    private CustomField getCustomField(String fieldName){
        final CustomField customField = new CustomField();
        customField.setCustomFieldType(CustomFieldType.BILLING_ACCOUNT);
        customField.setName(fieldName);
        return customField;
    }

    private CustomFieldValue getCustomFieldValue(String fieldName, String fieldValue){
        final CustomFieldValue customFieldValue = new CustomFieldValue();
        customFieldValue.setCustomFieldValueType(CustomFieldType.BILLING_ACCOUNT);
        customFieldValue.setCustomField(getCustomField(fieldName));
        customFieldValue.setValue(fieldValue);
        return customFieldValue;
    }
}
