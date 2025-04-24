package com.repsy.packagemanager.controller;

import com.repsy.packagemanager.entity.PackageEntity;
import com.repsy.packagemanager.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@Tag(name = "Package Deployment", description = "Package deployment operations")
public class DeploymentController {

    private final PackageService packageService;

    @PostMapping("/{packageName}/{version}")
    @Operation(summary = "Deploy a new package version")
    public ResponseEntity<PackageEntity> deployPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "meta", required = false) String metaJson) {
        PackageEntity packageEntity = packageService.deployPackage(packageName, version, file, metaJson);
        return ResponseEntity.ok(packageEntity);
    }
} 