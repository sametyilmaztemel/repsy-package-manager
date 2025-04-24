package com.repsy.packagemanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import com.repsy.packagemanager.service.PackageService;
import com.repsy.packagemanager.config.SecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DownloadController.class)
@Import(SecurityConfig.class)
class DownloadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PackageService packageService;

    @Test
    @WithMockUser
    void testDownloadPackage() throws Exception {
        String packageName = "test-package";
        String version = "1.0.0";
        byte[] packageContent = "test content".getBytes();
        
        // Create a custom ByteArrayResource with a filename
        Resource resource = new ByteArrayResource(packageContent) {
            @Override
            public String getFilename() {
                return packageName + "-" + version + ".zip";
            }
        };

        when(packageService.downloadPackage(packageName, version)).thenReturn(resource);

        mockMvc.perform(get("/api/packages/{packageName}/{version}/download", packageName, version))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + packageName + "-" + version + ".zip\""))
                .andExpect(content().bytes(packageContent));
    }
} 