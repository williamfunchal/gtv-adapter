package com.consensus.gtvadapter.module.poller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.consensus.gtvadapter.common.models.dto.customer.IspS3CustomerDTO;
import com.consensus.gtvadapter.common.models.rawdata.IspRawDataCustomer;
import com.consensus.gtvadapter.poller.mapper.ISPDataReadyMapper;
import com.consensus.gtvadapter.poller.service.ISPDataService;
import com.consensus.gtvadapter.poller.service.S3ReaderService;
import com.consensus.gtvadapter.poller.sqs.ISPDataPublishService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ISPDataServiceTest {
    @Mock
    private S3ReaderService<IspS3CustomerDTO> s3ReaderService;

    @Mock
    private ISPDataReadyMapper ispDataReadyMapper;

    @Mock
    private ISPDataPublishService ispDataPublishService;

    private ISPDataService ispDataService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ispDataService = new ISPDataService(ispDataPublishService, s3ReaderService, ispDataReadyMapper);
    }

    @Test
    public void testFetchISPData() throws IOException, URISyntaxException {
        List<IspS3CustomerDTO> ispS3CustomerDTOs = List.of(IspS3CustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .emailAddress("johndoe@example.com")
                .build()
            );
        when(s3ReaderService.readCsvFromLocalResource(IspS3CustomerDTO.class)).thenReturn(ispS3CustomerDTOs);
        when(ispDataReadyMapper.map(ispS3CustomerDTOs.get(0))).thenReturn(IspRawDataCustomer.builder().build());

        ispDataService.fetchISPData();

        verify(ispDataReadyMapper).map(ispS3CustomerDTOs.get(0));
        verify(ispDataPublishService).publishMessageToQueue("{}", null);
    }
}
