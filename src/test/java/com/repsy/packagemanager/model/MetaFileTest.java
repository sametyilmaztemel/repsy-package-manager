package com.repsy.packagemanager.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetaFileTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testMetaFileSerialization() throws Exception {
        // Create test data
        MetaFile metaFile = new MetaFile();
        metaFile.setName("test-package");
        metaFile.setVersion("1.0.0");
        metaFile.setAuthor("test-author");
        metaFile.setDescription("Test package description");
        metaFile.setDependencies(Arrays.asList("dep1", "dep2"));

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(metaFile);

        // Verify JSON content
        assertTrue(json.contains("\"name\":\"test-package\""));
        assertTrue(json.contains("\"version\":\"1.0.0\""));
        assertTrue(json.contains("\"author\":\"test-author\""));
        assertTrue(json.contains("\"description\":\"Test package description\""));
        assertTrue(json.contains("\"dependencies\":[\"dep1\",\"dep2\"]"));
    }

    @Test
    void testMetaFileDeserialization() throws Exception {
        // Test JSON
        String json = """
            {
                "name": "test-package",
                "version": "1.0.0",
                "author": "test-author",
                "description": "Test package description",
                "dependencies": ["dep1", "dep2"]
            }
            """;

        // Deserialize from JSON
        MetaFile metaFile = objectMapper.readValue(json, MetaFile.class);

        // Verify object content
        assertEquals("test-package", metaFile.getName());
        assertEquals("1.0.0", metaFile.getVersion());
        assertEquals("test-author", metaFile.getAuthor());
        assertEquals("Test package description", metaFile.getDescription());
        assertEquals(Arrays.asList("dep1", "dep2"), metaFile.getDependencies());
    }
} 