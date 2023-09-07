package com.consensus.gtvadapter.common.models.event;

import com.consensus.gtvadapter.common.models.rawdata.DataOperation;
import lombok.Data;

@Data
public abstract class IspDataReadyEvent<T> extends AdapterEvent {

    protected String tableName;
    protected DataOperation operation;
    protected T data;
}
