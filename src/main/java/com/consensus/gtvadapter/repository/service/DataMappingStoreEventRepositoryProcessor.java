package com.consensus.gtvadapter.repository.service;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.common.models.dto.IspGtvMappingDTO;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.DataMappingStoreEvent;
import com.consensus.gtvadapter.repository.storage.CustomerEventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DataMappingStoreEventRepositoryProcessor implements RepositoryProcessor<DataMappingStoreEvent> {

    private final CustomerEventsRepository customerEventsRepository;

    @Override
    public String eventType() {
        return DataMappingStoreEvent.TYPE;
    }

    @Override
    public AdapterEvent process(DataMappingStoreEvent event) {
        IspGtvMapping ispGtvMapping = event.getIspGtvMapping();
        IspGtvMappingDTO ispGtvMappingDTO = new IspGtvMappingDTO();
        ispGtvMappingDTO.setMappedData(ispGtvMapping.getMappedData());
        ispGtvMappingDTO.setRawData(ispGtvMapping.getRawData());
        ispGtvMappingDTO.setCorrelationId(event.getCorrelationId());
        customerEventsRepository.save(ispGtvMappingDTO);
        return event;
    }
}
