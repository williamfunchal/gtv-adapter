package com.consensus.gtvadapter.module.poller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.consensus.gtvadapter.config.AwsS3Properties;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ReaderService<T> {
    private AmazonS3 s3Client;

    private String bucketName;
    private String prefix;

    @Autowired
    public S3ReaderService(AmazonS3 s3Client, final AwsS3Properties awsS3Properties) {
        this.s3Client = s3Client;
        this.bucketName = awsS3Properties.getBucketName();
        this.prefix = awsS3Properties.getPrefix();
    }


    public List<T> readCsvFromS3(Class<T> type) throws IOException {
        List<T> objects = new ArrayList<>();

        List<S3ObjectSummary> s3ObjectSummaries = s3Client.listObjects(bucketName, prefix).getObjectSummaries();
        s3ObjectSummaries.sort(Comparator.comparing(S3ObjectSummary::getKey));
        
        for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {
            String key = s3ObjectSummary.getKey();

            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            objects.addAll(csvToBean.parse());

        }

        return objects;
    }
}
