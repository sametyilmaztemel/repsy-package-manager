package com.repsy.packagemanager.controller;

import com.repsy.packagemanager.entity.PackageEntity;
import com.repsy.packagemanager.service.PackageService;
import com.repsy.packagemanager.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeploymentController.class)
@Import(SecurityConfig.class)
class DeploymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PackageService packageService;

    @Test
    @WithMockUser
    void testDeployPackage() throws Exception {
        // Prepare test data
        String packageName = "test-package";
        String version = "1.0.0";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.rep",
                "application/octet-stream",
                "test content".getBytes()
        );
        String metaJson = "{\"name\":\"test-package\",\"version\":\"1.0.0\",\"author\":\"test-author\"}";

        // Mock service response
        PackageEntity mockPackage = new PackageEntity();
        mockPackage.setName(packageName);
        mockPackage.setVersion(version);
        when(packageService.deployPackage(eq(packageName), eq(version), any(), eq(metaJson)))
                .thenReturn(mockPackage);

        // Perform request
        mockMvc.perform(multipart("/api/packages/{packageName}/{version}", packageName, version)
                        .file(file)
                        .param("meta", metaJson))
                .andExpect(status().isOk());
    }
} 