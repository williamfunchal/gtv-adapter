package com.consensus.gtvadapter.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GtvConstants {
    public static final String ACCOUNT_CREATE_API = "/billing/2/billing-accounts";
    public static final String BILLING_PURPOSE = "BILLING";    
    public static final String PRIMARY_PURPOSE = "PRIMARY";

    public static class HttpHeaders {
        public static String GTV_API_KEY = "X-Api-Key";
    }
}
