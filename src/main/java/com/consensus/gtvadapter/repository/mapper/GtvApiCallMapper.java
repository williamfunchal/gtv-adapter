package com.consensus.gtvadapter.repository.mapper;

import com.consensus.gtvadapter.common.models.event.gtv.response.GtvResponseData;
import com.consensus.gtvadapter.common.models.event.isp.update.BaseDataUpdateEvent;
import com.consensus.gtvadapter.common.models.gtv.GtvData;
import com.consensus.gtvadapter.repository.entities.GtvApiCall;
import com.consensus.gtvadapter.repository.entities.GtvApiCallStatus;
import com.consensus.gtvadapter.repository.entities.GtvApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = {GtvApiCallStatus.class})
public abstract class GtvApiCallMapper extends BaseEventMapper {

    @Mapping(target = "body", source = "body", qualifiedByName = "convertToAttributeValue")
    @Mapping(target = "result", source = "result")
    @Mapping(target = "status", expression = "java(GtvApiCallStatus.COMPLETED)")
    public abstract GtvApiCall toGtvApiCall(BaseDataUpdateEvent<? extends GtvData> updateEvent);

    protected abstract GtvApiResponse toGtvApiResponse(GtvResponseData gtvResponseData);
}
