package com.consensus.gtvadapter.common.models.event.isp.update;

import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.gtv.response.GtvResponseData;
import com.consensus.gtvadapter.common.models.gtv.GtvData;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
public abstract class BaseDataUpdateEvent<T extends GtvData> extends AdapterEvent {

    protected String api;
    protected HttpMethod method;
    protected T body;
    protected GtvResponseData result;
}
