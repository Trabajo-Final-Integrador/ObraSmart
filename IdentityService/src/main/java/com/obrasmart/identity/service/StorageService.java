package com.obrasmart.identity.service;

import java.io.InputStream;

public interface StorageService {
    void putObject(String key, InputStream stream, String contentType, long sizeBytes);
    InputStream getObject(String key);
    void deleteObject(String key);
}
