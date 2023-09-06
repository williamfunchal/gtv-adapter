package com.consensus.gtvadapter.processor.mapper;

import com.consensus.gtvadapter.common.models.gtv.account.AccountCreationRequestBody;
import com.consensus.gtvadapter.common.models.gtv.account.BillCycle;
import com.consensus.gtvadapter.common.models.gtv.account.BillCycleType;
import com.consensus.gtvadapter.common.models.gtv.account.BillType;
import com.consensus.gtvadapter.common.models.gtv.account.BillingAccountCategory;
import com.consensus.gtvadapter.common.models.gtv.account.CurrencyCode;
import com.consensus.gtvadapter.common.models.gtv.account.CustomField;
import com.consensus.gtvadapter.common.models.gtv.account.CustomFieldIds;
import com.consensus.gtvadapter.common.models.gtv.account.CustomFieldType;
import com.consensus.gtvadapter.common.models.gtv.account.CustomFieldValue;
import com.consensus.gtvadapter.common.models.gtv.account.EmailAddress;
import com.consensus.gtvadapter.common.models.gtv.account.PartyType;
import com.consensus.gtvadapter.common.models.gtv.account.PaymentTerm;
import com.consensus.gtvadapter.common.models.gtv.account.PostalAddress;
import com.consensus.gtvadapter.common.models.gtv.account.ResponsibleParty;
import com.consensus.gtvadapter.common.models.rawdata.IspCustomerData;
import com.consensus.gtvadapter.config.properties.IspGtvMapsProperties;
import com.consensus.gtvadapter.processor.persistence.model.J2CorpProfile;
import com.consensus.gtvadapter.processor.persistence.model.JbcBillingEntity;
import com.consensus.gtvadapter.processor.persistence.repository.BillingEntityRepository;
import com.consensus.gtvadapter.processor.persistence.repository.CorpProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.consensus.gtvadapter.util.GtvConstants.BillingSystems.CIA_BILLING_SYSTEM;
import static com.consensus.gtvadapter.util.GtvConstants.BillingSystems.CIA_OFFER_CODES;
import static com.consensus.gtvadapter.util.GtvConstants.BillingSystems.CORP_AUTO_PREFIX;
import static com.consensus.gtvadapter.util.GtvConstants.BillingSystems.RED_PEPPER_BILLING_SYSTEM;

@Component
@RequiredArgsConstructor
class AccountMapper {

    private static final DateTimeFormatter ISP_DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
    private static final DateTimeFormatter CUSTOM_FIELD_DATE_PATTERN = DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(ZoneId.of("UTC"));
    private final IspGtvMapsProperties mappingProperties;
    private final BillingEntityRepository billingEntityRepository;
    private final CorpProfileRepository corpProfileRepository;

    public AccountCreationRequestBody toAccountCreationRequestBody(IspCustomerData ispCustomerData) {
        final JbcBillingEntity billingEntity = billingEntityRepository.findBillingEntityByCustomerKey(Long.valueOf(ispCustomerData.getCustomerkey()));
        final J2CorpProfile corpProfile = corpProfileRepository.findByResellerId(ispCustomerData.getResellerId());
        final AccountCreationRequestBody accountCreationRequestBody = new AccountCreationRequestBody();
        accountCreationRequestBody.setResponsibleParty(getResponsibleParty(ispCustomerData));
        final Instant startDate = convertToInstant(ispCustomerData.getStartDate());
        accountCreationRequestBody.setStartDate(startDate);
        accountCreationRequestBody.setCurrencyCode(CurrencyCode.valueOf(ispCustomerData.getCurrencyCode()));
        accountCreationRequestBody.setBillCycle(getBillCycle());
        accountCreationRequestBody.setBillType(BillType.NONE);
        accountCreationRequestBody.setBillingAccountCategory(getBillingAccountCategory(billingEntity));
        accountCreationRequestBody.setPaymentTerm(getPaymentTerms(corpProfile.getPaymentTerms()));
        accountCreationRequestBody.setCustomFieldValues(getCustomFiledValues(ispCustomerData.getResellerId(), CUSTOM_FIELD_DATE_PATTERN.format(startDate), billingEntity.getOrgId(), corpProfile.getOfferCode()));

        return accountCreationRequestBody;
    }

    private BillCycle getBillCycle() {
        final BillCycle billCycle = new BillCycle();
        billCycle.setId(mappingProperties.getBillCycleId());
        billCycle.setBillCycleType(BillCycleType.MONTHLY);
        return billCycle;
    }

    private BillingAccountCategory getBillingAccountCategory(JbcBillingEntity billingEntity) {
        final BillingAccountCategory billingAccountCategory = new BillingAccountCategory();
        final Map<String, String> accountCategories = mappingProperties.getAccountCategories();
        billingAccountCategory.setId(accountCategories.get(billingEntity.getCategoriesMappingKey()));
        return billingAccountCategory;
    }

    private ResponsibleParty getResponsibleParty(IspCustomerData ispCustomerData) {
        final ResponsibleParty responsibleParty = new ResponsibleParty();
        responsibleParty.setPartyType(PartyType.ORGANIZATION);
        responsibleParty.setExternalCustomerNum(ispCustomerData.getCustomerkey());
        responsibleParty.setOrganizationName(ispCustomerData.getCompany());
        final PostalAddress postalAddress = new PostalAddress();
        postalAddress.setCountry(new Locale("en", ispCustomerData.getCountry()).getISO3Country());
        postalAddress.setLine1(ispCustomerData.getAddressLine1());
        postalAddress.setLine2(ispCustomerData.getAddressLine2());
        postalAddress.setCity(ispCustomerData.getCity());
        postalAddress.setRegionOrState(ispCustomerData.getMailRegion());
        postalAddress.setPostalCode(ispCustomerData.getMailCode());
        final EmailAddress emailAddress = new EmailAddress();
        emailAddress.setEmail(ispCustomerData.getEmailAddress());
        responsibleParty.setAddresses(List.of(postalAddress, emailAddress));
        return responsibleParty;
    }

    private Instant convertToInstant(String date) {
        LocalDateTime localDateTime = LocalDate.parse(date, ISP_DATE_PATTERN).atStartOfDay();
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    private List<CustomFieldValue> getCustomFiledValues(String resellerId, String startDate, Long orgId, String offerCode) {
        List<CustomFieldValue> customFieldValues = new ArrayList<>();
        final CustomFieldIds customFieldIds = mappingProperties.getCustomFieldIds();
        final Map<Long, String> businessUnits = mappingProperties.getBusinessUnits();
        customFieldValues.add(getCustomFieldValue("CCSI CorpID", customFieldIds.getCorpId(), resellerId));
        customFieldValues.add(getCustomFieldValue("CCSI Offer Code Name", customFieldIds.getOfferCode(), offerCode));
        customFieldValues.add(getCustomFieldValue("CCSI Legacy Billing System", customFieldIds.getLegacyBillingSystem(), getLegacyBillingSystem(offerCode)));
        customFieldValues.add(getCustomFieldValue("CCSI Account Start Date", customFieldIds.getAccountStartDate(), startDate));
        customFieldValues.add(getCustomFieldValue("DNE Minimum Commitment", customFieldIds.getMinCommitmentSubs(), corpProfileRepository.findMinCommitmentSubscription(resellerId).toString()));
        customFieldValues.add(getCustomFieldValue("CCSI Marketplace ID", customFieldIds.getMarketplaceId(), "TBD"));
        customFieldValues.add(getCustomFieldValue("CCSI Business Unit", customFieldIds.getBusinessUnit(), businessUnits.get(orgId)));
        return customFieldValues;
    }

    private CustomField getCustomField(String fieldName, String fieldId) {
        final CustomField customField = new CustomField();
        customField.setCustomFieldType(CustomFieldType.BILLING_ACCOUNT);
        customField.setName(fieldName);
        customField.setId(fieldId);
        return customField;
    }

    private CustomFieldValue getCustomFieldValue(String fieldName, String fieldId, String fieldValue) {
        final CustomFieldValue customFieldValue = new CustomFieldValue();
        customFieldValue.setCustomFieldValueType(CustomFieldType.BILLING_ACCOUNT);
        customFieldValue.setCustomField(getCustomField(fieldName, fieldId));
        customFieldValue.setValue(fieldValue);
        return customFieldValue;
    }

    private PaymentTerm getPaymentTerms(String paymentTerm){
        final String paymentTermKey = paymentTerm.replace(" ", "");
        final String paymentTermsId = mappingProperties.getPaymentTerms().get(paymentTermKey);
        return new PaymentTerm(paymentTermsId);
    }

    private String getLegacyBillingSystem(String offerCode){
        if (null == offerCode ){
            return Strings.EMPTY;
        }else if (offerCode.startsWith(CORP_AUTO_PREFIX) || CIA_OFFER_CODES.contains(offerCode)){
            return CIA_BILLING_SYSTEM;
        }else{
            return RED_PEPPER_BILLING_SYSTEM;
        }
    }
}
