package com.consensus.gtvadapter.config;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

@Getter
public class AwsS3Properties {
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.prefix}")
    private String prefix;
}
