package com.consensus.gtvadapter.module.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.consensus.gtvadapter.GtvAdapterApplication;
import com.consensus.gtvadapter.common.models.dto.poller.IspS3CustomerDTO;
import com.consensus.gtvadapter.module.poller.service.CsvParserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CsvParserServiceTest {

    @Autowired
    private CsvParserService<IspS3CustomerDTO> csvParserService;

    @Test
    public void parseCsvFileTest() throws IOException {
        // List<IspS3CustomerDTO> customers = csvParserService.parseCsvFile("customer.csv", IspS3CustomerDTO.class);
        // assertEquals(2, customers.size());

        // IspS3CustomerDTO customer1 = customers.get(0);
        // assertEquals("John", customer1.getFirstName());
        // assertEquals("Doe", customer1.getLastName());
        // assertEquals("john.doe@example.com", customer1.getEmailAddress());
        // assertEquals("1234567890", customer1.getMobileNumber());

        // IspS3CustomerDTO customer2 = customers.get(1);
        // assertEquals("Jane", customer2.getFirstName());
        // assertEquals("Doe", customer2.getLastName());
        // assertEquals("jane.doe@example.com", customer2.getEmailAddress());
        // assertEquals("0987654321", customer2.getMobileNumber());
    }
}
