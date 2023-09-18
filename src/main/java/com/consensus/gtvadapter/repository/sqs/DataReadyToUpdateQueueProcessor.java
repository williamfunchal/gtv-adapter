package com.consensus.gtvadapter.repository.sqs;

import com.consensus.gtvadapter.config.properties.QueueProperties;
import com.consensus.gtvadapter.repository.service.RepositoryEventProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataReadyToUpdateQueueProcessor extends BaseMessageQueueProcessor {

    public DataReadyToUpdateQueueProcessor(QueueProperties queueProperties, ObjectMapper objectMapper,
            RepositoryEventProcessingService repositoryEventProcessingService) {
        super(queueProperties.getDataReadyToUpdate(), objectMapper, repositoryEventProcessingService);
    }
}
