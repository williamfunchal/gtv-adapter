package com.consensus.gtvadapter.module.poller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.consensus.gtvadapter.config.AwsS3Properties;
import com.consensus.gtvadapter.poller.service.S3ReaderService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class S3ReaderServiceTest {
    private S3ReaderService<TestObject> s3ReaderService;

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private AwsS3Properties awsS3Properties;

    @BeforeEach
    public void setUp() {
        s3Client = mock(AmazonS3.class);
        awsS3Properties = new AwsS3Properties();
        ReflectionTestUtils.setField(awsS3Properties, "bucketName", "test-bucket");
        ReflectionTestUtils.setField(awsS3Properties, "prefix", "test-prefix/");
        s3ReaderService = new S3ReaderService<>(s3Client, awsS3Properties);
    }

    @Test
    public void testReadCsvFromS3() throws IOException {
        // Mock S3ObjectSummaries
        List<S3ObjectSummary> s3ObjectSummaries = new ArrayList<>();
        S3ObjectSummary s3ObjectSummary1 = new S3ObjectSummary();
        s3ObjectSummary1.setKey("test-prefix/test-file1.csv");
        s3ObjectSummaries.add(s3ObjectSummary1);
        S3ObjectSummary s3ObjectSummary2 = new S3ObjectSummary();
        s3ObjectSummary2.setKey("test-prefix/test-file2.csv");
        s3ObjectSummaries.add(s3ObjectSummary2);
        when(s3Client.listObjects("test-bucket", "test-prefix/")).thenReturn(mock(ObjectListing.class));
        when(s3Client.listObjects("test-bucket", "test-prefix/").getObjectSummaries()).thenReturn(s3ObjectSummaries);

        // Mock S3ObjectInputStreams
        String csv1 = "id,name\n1,test1\n2,test2\n";
        S3ObjectInputStream inputStream1 = new S3ObjectInputStream(new ByteArrayInputStream(csv1.getBytes()), null);
        String csv2 = "id,name\n3,test3\n4,test4\n";
        S3ObjectInputStream inputStream2 = new S3ObjectInputStream(new ByteArrayInputStream(csv2.getBytes()), null);
        when(s3Client.getObject(new GetObjectRequest("test-bucket", "test-prefix/test-file1.csv"))).thenReturn(mock(S3Object.class));
        when(s3Client.getObject(new GetObjectRequest("test-bucket", "test-prefix/test-file1.csv")).getObjectContent()).thenReturn(inputStream1);
        when(s3Client.getObject(new GetObjectRequest("test-bucket", "test-prefix/test-file2.csv"))).thenReturn(mock(S3Object.class));
        when(s3Client.getObject(new GetObjectRequest("test-bucket", "test-prefix/test-file2.csv")).getObjectContent()).thenReturn(inputStream2);

        // Mock CsvToBean
        BufferedReader csvToBean1BufferedReader = new BufferedReader(new InputStreamReader(inputStream1));
        BufferedReader csvToBean2BufferedReader = new BufferedReader(new InputStreamReader(inputStream2));
        CsvToBean<TestObject> csvToBean1 = new CsvToBeanBuilder<TestObject>(csvToBean1BufferedReader)
                .withType(TestObject.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        CsvToBean<TestObject> csvToBean2 = new CsvToBeanBuilder<TestObject>(csvToBean2BufferedReader)
                .withType(TestObject.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        when(s3ReaderService.mapS3ObjectSummaryToCsvToBean(s3ObjectSummary1, TestObject.class)).thenReturn(csvToBean1);
        when(s3ReaderService.mapS3ObjectSummaryToCsvToBean(s3ObjectSummary2, TestObject.class)).thenReturn(csvToBean2);

        // Test readCsvFromS3
        List<TestObject> objects = s3ReaderService.readCsvFromS3(TestObject.class);
        assertEquals(4, objects.size());
        assertEquals(1, objects.get(0).getId());
        assertEquals("test1", objects.get(0).getName());
        assertEquals(2, objects.get(1).getId());
        assertEquals("test2", objects.get(1).getName());
        assertEquals(3, objects.get(2).getId());
        assertEquals("test3", objects.get(2).getName());
        assertEquals(4, objects.get(3).getId());
        assertEquals("test4", objects.get(3).getName());
    }
}
