package com.consensus.gtvadapter.poller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ReaderService<T> {
    private final AmazonS3 s3Client;
    private final AwsS3Properties awsS3Properties;
    private final String resourceFolder = "local-infra/aws/billplatform/projects/s3/initial-data/__files/ISPPOWER.ISPCUSTOMER";

    public List<T> readCsvFromS3(Class<T> type) throws IOException {
        List<T> objects = new ArrayList<>();

        List<S3ObjectSummary> s3ObjectSummaries = s3Client.listObjects(
                awsS3Properties.getBucketName(), 
                awsS3Properties.getPrefix())
            .getObjectSummaries();
        s3ObjectSummaries.sort(Comparator.comparing(S3ObjectSummary::getKey));
        
        for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {
            String key = s3ObjectSummary.getKey();

            S3Object s3Object = s3Client.getObject(new GetObjectRequest(awsS3Properties.getBucketName(), key));
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

    public List<T> readCsvFromLocalResource (Class<T> type) throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL directoryUrl = classLoader.getResource(resourceFolder);
        if (directoryUrl == null) {
            throw new IllegalArgumentException("Directory not found: " + resourceFolder);
        }
        Path directoryPath = Paths.get(directoryUrl.toURI());
        List<T> objects = new ArrayList<>();
        Files.walk(directoryPath)
            .filter(Files::isRegularFile)
            .sorted()
            .forEach(file -> {
                try {
                    log.info("Reading file: {}", file);
                    BufferedReader reader = Files.newBufferedReader(Paths.get(file.toUri()));
                    CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                            .withType(type)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
                    objects.addAll(csvToBean.parse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        
        return objects;
    }
}
