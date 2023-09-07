package com.consensus.gtvadapter.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

@UtilityClass
public class GtvConstants {

    @UtilityClass
    public static class HttpHeaders {
        public static final String GTV_API_KEY = "X-Api-Key";
    }

    @UtilityClass
    public static class BillingSystems {
        public static final Set<String> CIA_OFFER_CODES = Set.of("CORP_DEMO", "CONSENSUS", "CORP_AWS_USD", "CORP_VZN_USD", "CORP_VZG_USD", "JFAXEMPLOYEE98");
        public static final String CORP_AUTO_PREFIX = "CORPAUTO";
        public static final String CIA_BILLING_SYSTEM = "CIA";
        public static final String RED_PEPPER_BILLING_SYSTEM = "RedPepper";
    }

    @UtilityClass
    public static class SqsMessageAttributes {
        public static final String EVENT_ID = "event_id";
        public static final String EVENT_TYPE = "event_type";
        public static final String CORRELATION_ID = "correlation_id";
    }
}
