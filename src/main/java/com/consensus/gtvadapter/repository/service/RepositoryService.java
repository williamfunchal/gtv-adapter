package com.consensus.gtvadapter.repository.service;

import com.consensus.gtvadapter.common.models.IspGtvMapping;
import com.consensus.gtvadapter.common.models.event.AdapterEvent;
import com.consensus.gtvadapter.common.models.event.DataMappingStoreEvent;
import com.consensus.gtvadapter.repository.DataMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final DataMappingRepository dataMappingRepository;

    public void saveIspGtvMapping (AdapterEvent adapterEvent){

        if(DataMappingStoreEvent.TYPE.equals(adapterEvent.getEventType())) {
            final DataMappingStoreEvent dataMappingStoreEvent = (DataMappingStoreEvent) adapterEvent;
            final IspGtvMapping ispGtvMapping = dataMappingStoreEvent.getIspGtvMapping();
            dataMappingRepository.saveDataMapping(ispGtvMapping);
        }
    }

}
