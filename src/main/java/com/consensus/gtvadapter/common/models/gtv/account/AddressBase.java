package com.consensus.gtvadapter.common.models.gtv.account;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "address_type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PostalAddress.class, name = "postal"),
        @JsonSubTypes.Type(value = EmailAddress.class, name = "email")
})
public abstract class AddressBase {

    protected AddressType addressType;
    protected String purpose;
}
