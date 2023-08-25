package com.consensus.gtvadapter.common.models.rawdata;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonTypeName("customer")
public class IspRawDataCustomer extends IspRawData{
    private IspCustumerData data;

    @Builder
    public IspRawDataCustomer(String correlationId, String tableName, DataOperation operation, IspCustumerData data) {
        super(correlationId, tableName, operation);
        this.data = data;
    }
}
