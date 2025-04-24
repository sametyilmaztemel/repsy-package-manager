package com.repsy.packagemanager.storage;

import java.io.InputStream;
import java.io.OutputStream;

public interface StorageStrategy {
    void store(String packageId, String version, InputStream content);
    void retrieve(String packageId, String version, OutputStream output);
    void delete(String packageId, String version);
    boolean exists(String packageId, String version);
} 