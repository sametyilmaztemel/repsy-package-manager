package com.repsy.packagemanager.controller;

import com.repsy.packagemanager.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@Tag(name = "Package Download", description = "Package download operations")
public class DownloadController {

    private final PackageService packageService;

    @GetMapping("/{packageName}/{version}/download")
    @Operation(summary = "Download a package version")
    public ResponseEntity<Resource> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version) {
        Resource resource = packageService.downloadPackage(packageName, version);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
} 