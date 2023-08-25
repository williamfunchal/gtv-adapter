package com.consensus.gtvadapter.common.models.event;

import java.util.UUID;

import com.consensus.gtvadapter.common.models.rawdata.DataOperation;
import com.consensus.gtvadapter.common.models.rawdata.IspCustomerData;
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
public class IspNewCustomerEvent extends AdapterEvent {

    public static final String TYPE = "isp-new-customer";

    private String tableName;
    private DataOperation operation;
    private IspCustomerData data;

    @Builder
    public IspNewCustomerEvent(String eventType,UUID correlationId , String tableName, DataOperation operation, IspCustomerData data) {
        super(eventType, correlationId);
        this.tableName = tableName;
        this.operation = operation;
        this.data = data;
    }
}
