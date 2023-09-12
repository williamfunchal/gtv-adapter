package com.consensus.gtvadapter.common.models.event.isp.update;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.gtv.GtvData;
import com.consensus.gtvadapter.common.models.rawdata.DataOperation;
import com.consensus.gtvadapter.common.models.rawdata.IspData;
import lombok.Data;

@Data
public abstract class BaseIspDataUpdateEvent<R extends IspData, G extends GtvData> extends AdapterEvent {

    protected String tableName;
    protected DataOperation operation;
    protected R rawData;
    protected G gtvData;
}
