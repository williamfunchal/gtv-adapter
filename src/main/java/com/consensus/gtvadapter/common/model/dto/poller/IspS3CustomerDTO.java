package com.consensus.gtvadapter.common.model.dto.poller;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IspS3CustomerDTO {
    @CsvBindByName(column = "Op")
    private String op;

    @CsvBindByName(column = "CUSTOMERKEY")
    private String customerkey;

    @CsvBindByName(column = "CUSTOMERID")
    private String customerId;

    @CsvBindByName(column = "NAMEPREFIX")
    private String namePrefix;

    @CsvBindByName(column = "FIRSTNAME")
    private String firstName;

    @CsvBindByName(column = "LASTNAME")
    private String lastName;

    @CsvBindByName(column = "COMPANY")
    private String company;

    @CsvBindByName(column = "ADDRESSLINE1")
    private String addressLine1;

    @CsvBindByName(column = "ADDRESSLINE2")
    private String addressLine2;

    @CsvBindByName(column = "CITY")
    private String city;

    @CsvBindByName(column = "MAILREGION")
    private String mailRegion;

    @CsvBindByName(column = "MAILCODE")
    private String mailCode;

    @CsvBindByName(column = "COUNTRY")
    private String country;

    @CsvBindByName(column = "WORKNUMBER")
    private String workNumber;

    @CsvBindByName(column = "FAXNUMBER")
    private String faxNumber;

    @CsvBindByName(column = "HOMENUMBER")
    private String homeNumber;

    @CsvBindByName(column = "MOBILENUMBER")
    private String mobileNumber;

    @CsvBindByName(column = "PAGERNUMBER")
    private String pagerNumber;

    @CsvBindByName(column = "EMAILADDRESS")
    private String emailAddress;

    @CsvBindByName(column = "CONTACTMETHOD")
    private String contactMethod;

    @CsvBindByName(column = "COLLECTIONMETHOD")
    private String collectionMethod;

    @CsvBindByName(column = "BILLTYPE")
    private String billType;

    @CsvBindByName(column = "STATUS")
    private String status;

    @CsvBindByName(column = "ACTIVESERVICECOUNT")
    private String activeServiceCount;

    @CsvBindByName(column = "STARTDATE")
    private String startDate;

    @CsvBindByName(column = "ENDDATE")
    private String endDate;

    @CsvBindByName(column = "LASTCHARGEDATE")
    private String lastChargeDate;

    @CsvBindByName(column = "NEXTCHARGEDATE")
    private String nextChargeDate;

    @CsvBindByName(column = "LASTCOLLECTIONDATE")
    private String lastCollectionDate;

    @CsvBindByName(column = "NEXTCOLLECTIONDATE")
    private String nextCollectionDate;

    @CsvBindByName(column = "LASTBILLDATE")
    private String lastBillDate;

    @CsvBindByName(column = "NEXTBILLDATE")
    private String nextBillDate;

    @CsvBindByName(column = "BILLINGHIERARCHYKEY")
    private String billingHierarchyKey;

    @CsvBindByName(column = "INTERVAL")
    private String interval;

    @CsvBindByName(column = "INTERVALUNIT")
    private String intervalUnit;

    @CsvBindByName(column = "INTERVALDAY")
    private String intervalDay;

    @CsvBindByName(column = "DISCOUNTRATE")
    private String discountRate;

    @CsvBindByName(column = "DISCOUNTACCOUNTCODE")
    private String discountAccountCode;

    @CsvBindByName(column = "TAXEXEMPTFLAG")
    private String taxExemptFlag;

    @CsvBindByName(column = "TAXCODE")
    private String taxCode;

    @CsvBindByName(column = "NOTES")
    private String notes;

    @CsvBindByName(column = "RESELLERID")
    private String resellerId;

    @CsvBindByName(column = "CREATEUSERID")
    private String createUserId;

    @CsvBindByName(column = "CREATEDATETIME")
    private String createDateTime;

    @CsvBindByName(column = "EMAILTOSENDBUNDLE")
    private String emailToSendBundle;

    @CsvBindByName(column = "SENDSTATUS")
    private String sendStatus;

    @CsvBindByName(column = "TESTCODE")
    private String testCode;

    @CsvBindByName(column = "WEB_ORDER_NUMBER")
    private String webOrderNumber;

    @CsvBindByName(column = "LANGUAGECODE")
    private String languageCode;

    @CsvBindByName(column = "CURRENCYCODE")
    private String currencyCode;

    @CsvBindByName(column = "OEM_ID")
    private String oemId;

    @CsvBindByName(column = "NOEMAILFLAG")
    private String noEmailFlag;

    @CsvBindByName(column = "DONTEMAIL")
    private String dontEmail;

    @CsvBindByName(column = "EXCLUDEFROMREPORTSFLAG")
    private String excludeFromReportsFlag;

    @CsvBindByName(column = "EMAILFORMATCODE")
    private String emailFormatCode;

    @CsvBindByName(column = "CLOSEDATE")
    private String closeDate;

    @CsvBindByName(column = "LOCKUSERID")
    private String lockUserId;

    @CsvBindByName(column = "LOCKDATETIME")
    private String lockDateTime;

    @CsvBindByName(column = "CONF_ALLOWED")
    private String confAllowed;

    @CsvBindByName(column = "BILLABLE_FLAG")
    private String billableFlag;

    @CsvBindByName(column = "DONTSELLEMAIL")
    private String dontSellEmail;

    @CsvBindByName(column = "CONVEY_FLAG")
    private String conveyFlag;

    @CsvBindByName(column = "FOREIGN_CUSTOMER_KEY")
    private String foreignCustomerKey;

    @CsvBindByName(column = "DEPTCODE")
    private String deptCode;

    @CsvBindByName(column = "CANCELCODE")
    private String cancelCode;

    @CsvBindByName(column = "EMAILDOMAIN")
    private String emailDomain;

    @CsvBindByName(column = "LASTPERIODICCHARGEDATE")
    private String lastPeriodicChargeDate;

    @CsvBindByName(column = "NEXTPERIODICCHARGEDATE")
    private String nextPeriodicChargeDate;

    @CsvBindByName(column = "TIME_CODE")
    private String timeCode;

    @CsvBindByName(column = "TZ_CODE")
    private String tzCode;

    @CsvBindByName(column = "OFFER_CODE")
    private String offerCode;

    @CsvBindByName(column = "MONTH_INTERVAL")
    private String monthInterval;

    @CsvBindByName(column = "BILLINGPERIODENDDATE")
    private String billingPeriodEndDate;

    @CsvBindByName(column = "FRAUDEXPORTDATE")
    private String fraudExportDate;

    @CsvBindByName(column = "FORMATLOCALE")
    private String formatLocale;

    @CsvBindByName(column = "TAXEXEMPTID")
    private String taxExemptId;

    @CsvBindByName(column = "OS")
    private String os;

    @CsvBindByName(column = "REF_CODE")
    private String refCode;

    @CsvBindByName(column = "MODIFYUSERID")
    private String modifyUserId;

    @CsvBindByName(column = "MODIFYORACLEUSER")
    private String modifyOracleUser;

    @CsvBindByName(column = "MODIFYORACLETERMINAL")
    private String modifyOracleTerminal;

    @CsvBindByName(column = "MODIFYDATETIME")
    private String modifyDateTime;

    @CsvBindByName(column = "SALESUSERID")
    private String salesUserId;

    @CsvBindByName(column = "CUSTOMER_TYPE")
    private String customerType;

    @CsvBindByName(column = "ORIGINAL_OFFER_CODE")
    private String originalOfferCode;

    @CsvBindByName(column = "ORIGINAL_REF_CODE")
    private String originalRefCode;

    @CsvBindByName(column = "ORIGINAL_RESELLERID")
    private String originalResellerId;

    @CsvBindByName(column = "ORIGINAL_ENTRYLOCATION")
    private String originalEntryLocation;

    @CsvBindByName(column = "ORIGINAL_CREATIVE_KEY")
    private String originalCreativeKey;

    @CsvBindByName(column = "COMPANY_CODE")
    private String companyCode;

    @CsvBindByName(column = "MIGRATE_FLAG")
    private String migrateFlag;

    @CsvBindByName(column = "JCENTITYKEY")
    private String jcEntityKey;

    @CsvBindByName(column = "JCCHARSETKEY")
    private String jcCharsetKey;

    @CsvBindByName(column = "WEB_ACCOUNT_ID")
    private String webAccountId;

    @CsvBindByName(column = "WEB_PASSWORD_HASH")
    private String webPasswordHash;

    @CsvBindByName(column = "WEB_PASSWORD_ENCRYPT")
    private String webPasswordEncrypt;

    @CsvBindByName(column = "VERSION")
    private String version;

    @CsvBindByName(column = "SOURCE_APP")
    private String sourceApp;

    @CsvBindByName(column = "LOGIN_FLAGS")
    private String loginFlags;

    @CsvBindByName(column = "ACCOUNTCLOSERULE")
    private String accountCloseRule;
}