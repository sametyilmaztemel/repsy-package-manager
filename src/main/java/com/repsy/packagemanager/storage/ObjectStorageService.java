package com.repsy.packagemanager.storage;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
public class ObjectStorageService implements StorageStrategy {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name:repsy-packages}")
    private String bucketName;

    @Override
    public void store(String packageId, String version, InputStream content) {
        try {
            createBucketIfNotExists();
            String objectName = getObjectName(packageId, version);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(content, -1, 10485760)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to store package in MinIO", e);
        }
    }

    @Override
    public void retrieve(String packageId, String version, OutputStream output) {
        try {
            String objectName = getObjectName(packageId, version);
            try (InputStream input = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            )) {
                input.transferTo(output);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve package from MinIO", e);
        }
    }

    @Override
    public void delete(String packageId, String version) {
        try {
            String objectName = getObjectName(packageId, version);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete package from MinIO", e);
        }
    }

    @Override
    public boolean exists(String packageId, String version) {
        try {
            String objectName = getObjectName(packageId, version);
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createBucketIfNotExists() throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    private String getObjectName(String packageId, String version) {
        return String.format("%s/%s.rep", packageId, version);
    }
} 