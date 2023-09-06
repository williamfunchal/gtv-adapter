package com.consensus.gtvadapter.config.properties;


import com.consensus.gtvadapter.common.models.gtv.account.CustomFieldIds;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("app.isp-gtv-maps")
public class IspGtvMapsProperties {

    private Map<String, String> accountCategories;
    private Map<Long, String> businessUnits;
    private Map<String, String> paymentTerms;
    private String billCycleId;
    private CustomFieldIds customFieldIds;

}
