package com.reneekbartlett.verisimilar.api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "application.security")
public class ApiKeyProperties {

    private Map<String, ApiKeyConfig> apiKeys;
    private boolean allowUrlApiKeys = false;

    @NotBlank
    private String sharedSecret = null;

    public boolean isAllowUrlApiKeys() {
        return allowUrlApiKeys;
    }

    public boolean hasSharedSecret() {
        return sharedSecret == null ? false : true;
    }

    public void setAllowUrlApiKeys(boolean allowUrlApiKeys) {
        this.allowUrlApiKeys = allowUrlApiKeys;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getSharedSecret() {
        return this.sharedSecret;
    }

    public Map<String, ApiKeyConfig> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Map<String, ApiKeyConfig> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public record ApiKeyConfig(String key, List<String> roles) {}

}
