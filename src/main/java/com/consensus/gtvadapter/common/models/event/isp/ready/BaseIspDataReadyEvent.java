package com.consensus.gtvadapter.common.models.event.isp.ready;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.rawdata.DataOperation;
import com.consensus.gtvadapter.common.models.rawdata.IspData;
import lombok.Data;

@Data
public abstract class BaseIspDataReadyEvent<T extends IspData> extends AdapterEvent {

    protected String tableName;
    protected DataOperation operation;
    protected T data;
}
