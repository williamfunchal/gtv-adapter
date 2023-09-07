package com.consensus.gtvadapter.common.models.dto;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import lombok.Data;

import java.util.UUID;

@Data
public class IspGtvMappingDTO extends IspGtvMapping {

    private String correlationId;
    private String eventId;
}
