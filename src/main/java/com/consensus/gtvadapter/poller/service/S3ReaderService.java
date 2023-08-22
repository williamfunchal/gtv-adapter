package com.consensus.gtvadapter.poller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    
    public List<T> readCsvFromS3(Class<T> type) throws IOException {
        List<T> objects = new ArrayList<>();
        this.createArchiveFolder();

        List<S3ObjectSummary> s3ObjectSummaries = this.readS3WithSortedObjects(type); 
        for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaries) {            
            CsvToBean<T> csvToBean = this.mapS3ObjectSummaryToCsvToBean(s3ObjectSummary, type);
            objects.addAll(csvToBean.parse());
            this.archiveS3Object(s3ObjectSummary);
        }

        return objects;
    }    

    // Read S3 bucket and return a list of objects sorted by name
    private List<S3ObjectSummary> readS3WithSortedObjects(Class<T> type) throws IOException {
        List<S3ObjectSummary> s3ObjectSummaries = s3Client.listObjects(
                awsS3Properties.getBucketName(),
                awsS3Properties.getPrefix())
            .getObjectSummaries();
        s3ObjectSummaries.sort(Comparator.comparing(S3ObjectSummary::getKey));
        
        return s3ObjectSummaries;
    }

    private void createArchiveFolder(){
        //verify if Archive folder exists. If not, create it
        try{
            if (!s3Client.doesObjectExist(awsS3Properties.getBucketName(), awsS3Properties.getPrefix() + "Archive/")) {
                s3Client.putObject(awsS3Properties.getBucketName(), awsS3Properties.getPrefix() + "Archive/", "");
            }
        }
        catch(Exception e){
            log.error("Error creating Archive folder: " + e.getMessage());
        }
    }

    // move S3 file read to Archive folder
    public void archiveS3Object(S3ObjectSummary s3ObjectSummary) { 
        try{
            String key = s3ObjectSummary.getKey();
            s3Client.copyObject(awsS3Properties.getBucketName(), key, awsS3Properties.getBucketName(), key.replace(awsS3Properties.getPrefix(), awsS3Properties.getPrefix() + "Archive/"));
            s3Client.deleteObject(awsS3Properties.getBucketName(), key);
        }
        catch(Exception e){
            log.error("Error moving file to Archive folder: " + e.getMessage());
        }
    }

    //Map S3ObjectSummary to CsvToBean
    public CsvToBean<T> mapS3ObjectSummaryToCsvToBean(S3ObjectSummary s3ObjectSummary, Class<T> type) {
        try{
            String key = s3ObjectSummary.getKey();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(awsS3Properties.getBucketName(), key));
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean;
        }
        catch(Exception e){
            log.error("Error mapping S3ObjectSummary to CsvToBean: " + e.getMessage());
            return null;
        }    
    }

}
