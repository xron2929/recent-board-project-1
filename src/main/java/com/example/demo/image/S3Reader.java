package com.example.demo.image;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class S3Reader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;


    public byte[] read(String key) {
        S3Object object = amazonS3.getObject(bucketName, key);
        InputStream inputStream = object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error reading S3 object", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // ignore
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
