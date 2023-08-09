package com.consensus.gtvadapter.module.poller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3ReaderService<T> {
    private AmazonS3 s3Client;

    @Value("${s3.bucketName}")
    private String bucketName;

    @Value("${s3.prefix}")
    private String prefix;

    public List<T> readCsvFromS3(Class<T> type) throws IOException {
        List<T> objects = new ArrayList<>();

        List<S3ObjectSummary> s3ObjectSummaries = s3Client.listObjects(bucketName, prefix).getObjectSummaries();
        for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {
            String key = s3ObjectSummary.getKey();
            if (key.endsWith(".csv")) {
                String[] parts = key.split("_");
                if (parts.length > 0) {
                    String timestampStr = parts[0];
                    try {
                        long timestamp = Long.parseLong(timestampStr);
                        if (timestamp == Instant.now().getEpochSecond()) {
                            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
                            S3ObjectInputStream inputStream = s3Object.getObjectContent();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                                    .withType(type)
                                    .withIgnoreLeadingWhiteSpace(true)
                                    .build();
                            objects.addAll(csvToBean.parse());
                        }
                    } catch (NumberFormatException e) {
                        // Ignore files with invalid timestamps
                    }
                }
            }
        }

        return objects;
    }
}
