package com.example.demo.image;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Component
public class S3FileDeleter {


    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;
    public void deleteFile(String objectKey) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "ap-northeast-2"))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        s3Client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
    }
    public void deleteFiles(List<String> objectKeys) {
        System.out.println("objectKeys.size() = " + objectKeys.size());
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "ap-northeast-2"))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        for (String objectKey : objectKeys) {
            taskExecutor.execute(() -> {
                String decode = null;
                try {
                    decode = URLDecoder.decode(objectKey, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                synchronized (s3Client) {
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName, decode));
                }
                System.out.println("decode = " + decode);
                System.out.println("objectKey = " + objectKey);
                System.out.println("Thread " + Thread.currentThread().getId() + " finished");
            });
        }
    }

}

