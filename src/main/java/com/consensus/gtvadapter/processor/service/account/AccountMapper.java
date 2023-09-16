package com.consensus.gtvadapter.processor.service.account;

import com.consensus.gtvadapter.common.models.gtv.account.*;
import com.consensus.gtvadapter.common.models.rawdata.CustomerIspData;
import com.consensus.gtvadapter.config.properties.IspGtvMapsProperties;
import com.consensus.gtvadapter.processor.persistence.entities.J2CorpProfile;
import com.consensus.gtvadapter.processor.persistence.entities.JbcBillingEntity;
import com.consensus.gtvadapter.processor.persistence.repository.BillingEntityRepository;
import com.consensus.gtvadapter.processor.persistence.repository.CorpProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.consensus.gtvadapter.common.models.gtv.account.BillCycleType.MONTHLY;
import static com.consensus.gtvadapter.common.models.gtv.account.CustomFieldType.BILLING_ACCOUNT;
import static com.consensus.gtvadapter.util.GtvConstants.BillingSystems.*;
import static java.time.ZoneOffset.UTC;
import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
class AccountMapper {

    private static final DateTimeFormatter ISP_DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
    private static final DateTimeFormatter CUSTOM_FIELD_DATE_PATTERN = DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(UTC);

    private final IspGtvMapsProperties mappingProperties;
    private final BillingEntityRepository billingEntityRepository;
    private final CorpProfileRepository corpProfileRepository;

    public AccountCreationGtvData toAccountCreationData(CustomerIspData ispCustomerData) {
        JbcBillingEntity billingEntity = billingEntityRepository.findBillingEntityByCustomerKey(Long.valueOf(ispCustomerData.getCustomerKey()));
        J2CorpProfile corpProfile = corpProfileRepository.findByResellerId(ispCustomerData.getResellerId());

        var startDate = convertToInstant(ispCustomerData.getStartDate());
        var paymentTerm = Optional.ofNullable(corpProfile)
                .map(J2CorpProfile::getPaymentTerms)
                .map(this::getPaymentTerms)
                .orElse(null);
        var offerCode = Optional.ofNullable(corpProfile)
                .map(J2CorpProfile::getOfferCode)
                .orElse(null);

        AccountCreationGtvData accountCreationGtvData = new AccountCreationGtvData();
        accountCreationGtvData.setResponsibleParty(getResponsibleParty(ispCustomerData));
        accountCreationGtvData.setStartDate(startDate);
        accountCreationGtvData.setCurrencyCode(CurrencyCode.valueOf(ispCustomerData.getCurrencyCode()));
        accountCreationGtvData.setBillCycle(getBillCycle());
        accountCreationGtvData.setBillType(BillType.NONE);
        accountCreationGtvData.setBillingAccountCategory(getBillingAccountCategory(billingEntity));
        accountCreationGtvData.setPaymentTerm(paymentTerm);
        accountCreationGtvData.setCustomFieldValues(getCustomFiledValues(ispCustomerData.getResellerId(), CUSTOM_FIELD_DATE_PATTERN.format(startDate), billingEntity.getOrgId(), offerCode));
        return accountCreationGtvData;
    }

    private BillCycle getBillCycle() {
        BillCycle billCycle = new BillCycle();
        billCycle.setId(mappingProperties.getBillCycleId());
        billCycle.setBillCycleType(MONTHLY);
        return billCycle;
    }

    private BillingAccountCategory getBillingAccountCategory(JbcBillingEntity billingEntity) {
        BillingAccountCategory billingAccountCategory = new BillingAccountCategory();
        Map<String, String> accountCategories = mappingProperties.getAccountCategories();
        billingAccountCategory.setId(accountCategories.get(billingEntity.getCategoriesMappingKey()));
        return billingAccountCategory;
    }

    private ResponsibleParty getResponsibleParty(CustomerIspData ispCustomerData) {
        ResponsibleParty responsibleParty = new ResponsibleParty();
        responsibleParty.setPartyType(PartyType.ORGANIZATION);
        responsibleParty.setExternalCustomerNum(ispCustomerData.getCustomerKey());
        responsibleParty.setOrganizationName(ispCustomerData.getCompany());
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setCountry(new Locale("en", ispCustomerData.getCountry()).getISO3Country());
        postalAddress.setLine1(ispCustomerData.getAddressLine1());
        postalAddress.setLine2(ispCustomerData.getAddressLine2());
        postalAddress.setCity(ispCustomerData.getCity());
        postalAddress.setRegionOrState(ispCustomerData.getMailRegion());
        postalAddress.setPostalCode(ispCustomerData.getMailCode());
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setEmail(ispCustomerData.getEmailAddress());
        responsibleParty.setAddresses(List.of(postalAddress, emailAddress));
        return responsibleParty;
    }

    private Instant convertToInstant(String date) {
        LocalDateTime localDateTime = LocalDate.parse(date, ISP_DATE_PATTERN).atStartOfDay();
        return localDateTime.toInstant(UTC);
    }

    private List<CustomFieldValue> getCustomFiledValues(String resellerId, String startDate, Long orgId,
            String offerCode) {
        List<CustomFieldValue> customFieldValues = new ArrayList<>();
        CustomFieldIds customFieldIds = mappingProperties.getCustomFieldIds();
        Map<Long, String> businessUnits = mappingProperties.getBusinessUnits();
        String minCommitment = Optional.ofNullable(corpProfileRepository.findMinCommitmentSubscription(resellerId))
                .map(String::valueOf)
                .orElse(null);

        customFieldValues.add(getCustomFieldValue("CCSI CorpID", customFieldIds.getCorpId(), resellerId));
        customFieldValues.add(getCustomFieldValue("CCSI Offer Code Name", customFieldIds.getOfferCode(), offerCode));
        customFieldValues.add(getCustomFieldValue("CCSI Legacy Billing System", customFieldIds.getLegacyBillingSystem(), getLegacyBillingSystem(offerCode)));
        customFieldValues.add(getCustomFieldValue("CCSI Account Start Date", customFieldIds.getAccountStartDate(), startDate));
        customFieldValues.add(getCustomFieldValue("DNE Minimum Commitment", customFieldIds.getMinCommitmentSubs(), minCommitment));
        customFieldValues.add(getCustomFieldValue("CCSI Marketplace ID", customFieldIds.getMarketplaceId(), "TBD"));
        customFieldValues.add(getCustomFieldValue("CCSI Business Unit", customFieldIds.getBusinessUnit(), businessUnits.get(orgId)));
        return customFieldValues;
    }

    private CustomField getCustomField(String fieldName, String fieldId) {
        CustomField customField = new CustomField();
        customField.setCustomFieldType(BILLING_ACCOUNT);
        customField.setName(fieldName);
        customField.setId(fieldId);
        return customField;
    }

    private CustomFieldValue getCustomFieldValue(String fieldName, String fieldId, String fieldValue) {
        CustomFieldValue customFieldValue = new CustomFieldValue();
        customFieldValue.setCustomFieldValueType(BILLING_ACCOUNT);
        customFieldValue.setCustomField(getCustomField(fieldName, fieldId));
        customFieldValue.setValue(fieldValue);
        return customFieldValue;
    }

    private PaymentTerm getPaymentTerms(String paymentTerm) {
        String paymentTermKey = paymentTerm.replace(" ", "");
        String paymentTermsId = mappingProperties.getPaymentTerms().get(paymentTermKey);
        return new PaymentTerm(paymentTermsId);
    }

    private String getLegacyBillingSystem(String offerCode) {
        if (!hasText(offerCode)) {
            return Strings.EMPTY;
        } else if (offerCode.startsWith(CORP_AUTO_PREFIX) || CIA_OFFER_CODES.contains(offerCode)) {
            return CIA_BILLING_SYSTEM;
        } else {
            return RED_PEPPER_BILLING_SYSTEM;
        }
    }
}
