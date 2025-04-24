package com.repsy.packagemanager.repository;

import com.repsy.packagemanager.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    Optional<PackageEntity> findByNameAndVersion(String name, String version);
    boolean existsByNameAndVersion(String name, String version);
    void deleteByNameAndVersion(String name, String version);
} 