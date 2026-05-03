package com.reneekbartlett.verisimilar.api.security.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "application.security")
public class ApiKeyProperties {

    private Map<String, ApiKeyConfig> apiKeys;

    private boolean allowUrlApiKeys = false;

    public boolean isAllowUrlApiKeys() {
        return allowUrlApiKeys;
    }

    public void setAllowUrlApiKeys(boolean allowUrlApiKeys) {
        this.allowUrlApiKeys = allowUrlApiKeys;
    }

    public Map<String, ApiKeyConfig> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(Map<String, ApiKeyConfig> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public static class ApiKeyConfig {
        private String key;
        private List<String> roles;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
