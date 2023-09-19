package com.consensus.gtvadapter.processor.service.usage;

import com.consensus.gtvadapter.common.models.gtv.usage.UsageCreationGtvData;
import com.consensus.gtvadapter.common.models.gtv.usage.UsageUom;
import com.consensus.gtvadapter.common.models.rawdata.IspUsageData;
import org.mapstruct.Mapper;

@Mapper(imports = {UsageUom.class})
public abstract class UsageMapper {

    public abstract UsageCreationGtvData toGtvData (IspUsageData ispUsageData);

}
