package com.repsy.storage.filesystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileSystemStorageStrategy implements StorageStrategy {

    private static final Logger log = LoggerFactory.getLogger(FileSystemStorageStrategy.class);

    private final Path rootLocation;

    public FileSystemStorageStrategy(@Value("${file.storage.location:./storage}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation);
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            log.error("Could not initialize file storage location", e);
            throw new RuntimeException("Could not initialize file storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            String filename = UUID.randomUUID().toString();
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(filename))
                    .normalize().toAbsolutePath();
            
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            
            Files.copy(file.getInputStream(), destinationFile);
            log.info("Stored file: {}", filename);
            return filename;
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public byte[] load(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            return Files.readAllBytes(file);
        } catch (IOException e) {
            log.error("Failed to load file: {}", filename, e);
            throw new RuntimeException("Failed to load file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
            log.info("Deleted file: {}", filename);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filename, e);
            throw new RuntimeException("Failed to delete file: " + filename, e);
        }
    }

    @Override
    public void store(String packageName, String version, String fileName, byte[] fileContent) {
        try {
            Path packagePath = getPackagePath(packageName, version);
            Files.createDirectories(packagePath);
            Path filePath = packagePath.resolve(fileName);
            Files.write(filePath, fileContent);
            log.info("Stored file: {} in {}/{}", fileName, packageName, version);
        } catch (IOException e) {
            log.error("Failed to store file: {}", fileName, e);
            throw new RuntimeException("Failed to store file: " + fileName, e);
        }
    }

    @Override
    public byte[] load(String packageName, String version, String fileName) {
        try {
            Path filePath = getPackagePath(packageName, version).resolve(fileName);
            if (!Files.exists(filePath)) {
                log.error("File not found: {}", filePath);
                return null;
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Failed to load file: {}", fileName, e);
            throw new RuntimeException("Failed to load file: " + fileName, e);
        }
    }

    @Override
    public boolean exists(String packageName, String version, String fileName) {
        Path filePath = getPackagePath(packageName, version).resolve(fileName);
        return Files.exists(filePath);
    }

    private Path getPackagePath(String packageName, String version) {
        return rootLocation.resolve(Paths.get(packageName, version));
    }
}
