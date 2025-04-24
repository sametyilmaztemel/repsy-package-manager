package com.repsy.packagemanager.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PackageEntityTest {

    @Test
    void testPackageEntityCreation() {
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setName("test-package");
        packageEntity.setVersion("1.0.0");
        packageEntity.setAuthor("test-author");
        packageEntity.setFileName("test.rep");
        packageEntity.setFileSize(1024L);
        packageEntity.setFileHash("abc123");
        packageEntity.setDescription("Test package");
        packageEntity.setDependencies("[]");

        assertNotNull(packageEntity);
        assertEquals("test-package", packageEntity.getName());
        assertEquals("1.0.0", packageEntity.getVersion());
        assertEquals("test-author", packageEntity.getAuthor());
        assertEquals("test.rep", packageEntity.getFileName());
        assertEquals(1024L, packageEntity.getFileSize());
        assertEquals("abc123", packageEntity.getFileHash());
        assertEquals("Test package", packageEntity.getDescription());
        assertEquals("[]", packageEntity.getDependencies());
        assertTrue(packageEntity.isActive());
    }
} 