package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomFieldValue {

    private CustomField customField;
    private CustomFieldType customFieldValueType;
    private String value;
}
