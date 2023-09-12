package com.consensus.gtvadapter.common.models.event.gtv.response;

import com.consensus.gtvadapter.common.models.event.gtv.request.BaseGtvRequest;
import com.consensus.gtvadapter.common.models.gtv.GtvData;
import lombok.Data;

@Data
public abstract class BaseGtvResponse<T extends GtvData> extends BaseGtvRequest<T> {

    protected GtvResponseData result;
}
