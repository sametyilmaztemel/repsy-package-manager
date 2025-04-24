package com.repsy.packagemanager.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileSystemStorageStrategy implements StorageStrategy {

    private final Path storageDirectory;

    public FileSystemStorageStrategy(@Value("${storage.file-system.directory:./storage}") String storageDirectory) {
        this.storageDirectory = Paths.get(storageDirectory).toAbsolutePath();
        createStorageDirectoryIfNotExists();
    }

    private void createStorageDirectoryIfNotExists() {
        try {
            Files.createDirectories(storageDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory", e);
        }
    }

    @Override
    public void store(String packageId, String version, InputStream content) {
        Path packagePath = getPackagePath(packageId, version);
        try {
            Files.createDirectories(packagePath.getParent());
            try (content) {
                Files.copy(content, packagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store package", e);
        }
    }

    @Override
    public void retrieve(String packageId, String version, OutputStream output) {
        Path packagePath = getPackagePath(packageId, version);
        try (InputStream input = Files.newInputStream(packagePath)) {
            input.transferTo(output);
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve package", e);
        }
    }

    @Override
    public void delete(String packageId, String version) {
        Path packagePath = getPackagePath(packageId, version);
        try {
            Files.deleteIfExists(packagePath);
            // Clean up empty parent directories
            Path parent = packagePath.getParent();
            while (parent != null && !parent.equals(storageDirectory) && Files.isDirectory(parent) && isEmptyDirectory(parent)) {
                Files.delete(parent);
                parent = parent.getParent();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete package", e);
        }
    }

    @Override
    public boolean exists(String packageId, String version) {
        return Files.exists(getPackagePath(packageId, version));
    }

    private Path getPackagePath(String packageId, String version) {
        return storageDirectory.resolve(packageId).resolve(version + ".rep");
    }

    private boolean isEmptyDirectory(Path directory) {
        try {
            return Files.isDirectory(directory) && !Files.list(directory).findAny().isPresent();
        } catch (IOException e) {
            return false;
        }
    }
} 