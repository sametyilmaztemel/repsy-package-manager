package com.repsy.packagemanager.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemStorageStrategyTest {

    @TempDir
    Path tempDir;
    private FileSystemStorageStrategy storageStrategy;

    @BeforeEach
    void setUp() {
        storageStrategy = new FileSystemStorageStrategy(tempDir.toString());
    }

    @Test
    void testStoreAndRetrieve() throws Exception {
        String packageId = "test-package";
        String version = "1.0.0";
        String content = "Test content";

        // Store
        storageStrategy.store(packageId, version, new ByteArrayInputStream(content.getBytes()));

        // Verify file exists
        assertTrue(storageStrategy.exists(packageId, version));

        // Retrieve
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        storageStrategy.retrieve(packageId, version, outputStream);
        assertEquals(content, outputStream.toString());
    }

    @Test
    void testDelete() throws Exception {
        String packageId = "test-package";
        String version = "1.0.0";
        String content = "Test content";

        // Store
        storageStrategy.store(packageId, version, new ByteArrayInputStream(content.getBytes()));
        assertTrue(storageStrategy.exists(packageId, version));

        // Delete
        storageStrategy.delete(packageId, version);
        assertFalse(storageStrategy.exists(packageId, version));
    }
} 