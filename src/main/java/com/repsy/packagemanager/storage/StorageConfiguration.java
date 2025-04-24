package com.repsy.packagemanager.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Value("${storage.strategy:file-system}")
    private String storageStrategy;

    @Bean
    public StorageStrategy storageStrategy(FileSystemStorageStrategy fileSystemStorageStrategy) {
        return switch (storageStrategy.toLowerCase()) {
            case "file-system" -> fileSystemStorageStrategy;
            case "minio" -> throw new UnsupportedOperationException("MinIO storage strategy not implemented yet");
            default -> throw new IllegalArgumentException("Unsupported storage strategy: " + storageStrategy);
        };
    }
} 