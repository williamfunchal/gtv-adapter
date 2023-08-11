package com.consensus.gtvadapter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class AwsS3Properties {
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.prefix}")
    private String prefix;
}
