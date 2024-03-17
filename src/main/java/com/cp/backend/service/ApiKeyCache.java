package com.cp.backend.service;

import org.springframework.stereotype.Service;

@Service
public class ApiKeyCache {

    private String apiKey;

    public void set(String apiKey) {
        this.apiKey = apiKey;
    }

    public String get() {
        return this.apiKey;
    }
}
