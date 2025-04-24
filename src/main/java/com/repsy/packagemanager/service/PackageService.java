package com.repsy.packagemanager.service;

import com.repsy.packagemanager.entity.PackageEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PackageService {
    PackageEntity deployPackage(String packageName, String version, MultipartFile file, String metaJson);
    PackageEntity getPackage(String packageName, String version);
    void deletePackage(String packageName, String version);
    Resource downloadPackage(String packageName, String version);
} 