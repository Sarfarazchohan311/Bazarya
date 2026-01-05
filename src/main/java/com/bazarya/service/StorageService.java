package com.bazarya.service;

import java.io.InputStream;

public interface StorageService {
    String upload(String key, InputStream inputStream, String contentType);
}
