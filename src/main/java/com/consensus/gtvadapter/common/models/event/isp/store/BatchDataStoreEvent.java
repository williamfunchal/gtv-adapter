package com.consensus.gtvadapter.common.models.event.isp.store;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import lombok.Data;

import java.util.List;

@Data
public abstract class BatchDataStoreEvent<R,G> extends AdapterEvent {
    protected List<EventBatch<R,G>> data;

}
