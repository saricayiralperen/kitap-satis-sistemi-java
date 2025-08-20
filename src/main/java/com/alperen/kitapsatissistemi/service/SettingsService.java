package com.alperen.kitapsatissistemi.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SettingsService {

    private final ConcurrentHashMap<String, Boolean> settings = new ConcurrentHashMap<>();

    public SettingsService() {
        // Default settings
        settings.put("loginRateLimit", true);
        settings.put("registerRateLimit", true);
    }

    public boolean isLoginRateLimitEnabled() {
        return settings.getOrDefault("loginRateLimit", true);
    }

    public void setLoginRateLimitEnabled(boolean enabled) {
        settings.put("loginRateLimit", enabled);
    }

    public boolean isRegisterRateLimitEnabled() {
        return settings.getOrDefault("registerRateLimit", true);
    }

    public void setRegisterRateLimitEnabled(boolean enabled) {
        settings.put("registerRateLimit", enabled);
    }
}