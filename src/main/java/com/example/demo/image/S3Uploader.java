package com.example.demo.image;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public String upload(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String key = uuid + "_" + file.getOriginalFilename();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        InputStream inputStream = file.getInputStream();
        System.out.println("inputStream = " + inputStream);
        amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata));
        return amazonS3.getUrl(bucketName, key).toString().split("https://s3-fastcampus.s3.ap-northeast-2.amazonaws.com/")[1];
    }
}
