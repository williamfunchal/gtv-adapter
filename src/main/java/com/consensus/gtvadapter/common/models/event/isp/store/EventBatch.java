package com.consensus.gtvadapter.common.models.event.isp.store;

import com.consensus.gtvadapter.common.models.gtv.GtvData;
import com.consensus.gtvadapter.common.models.rawdata.IspData;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventBatch {

    private String eventId;
    private String correlationId;
    private IspData rawData;
    private GtvData gtvData;

}
