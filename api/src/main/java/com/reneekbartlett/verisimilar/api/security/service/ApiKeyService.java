package com.reneekbartlett.verisimilar.api.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.reneekbartlett.verisimilar.api.security.ApiKeyProperties;
//import com.reneekbartlett.verisimilar.api.security.ApiKeyProperties.ApiKeyConfig;

import java.util.List;
//import java.util.Map;
import java.util.Optional;

@Service
public class ApiKeyService {

    private final ApiKeyProperties properties;

    public ApiKeyService(ApiKeyProperties properties) {
        this.properties = properties;
    }

    public boolean isValid(String key) {
        return properties.getApiKeys().values().stream()
                .anyMatch(cfg -> cfg.key().equals(key));
    }

    public Optional<String> getClientId(String key) {
        return properties.getApiKeys().entrySet().stream()
                .filter(e -> e.getValue().key().equals(key))
                .map(e -> e.getKey())
                .findFirst();
    }

    public List<GrantedAuthority> getAuthorities(String key) {
        return properties.getApiKeys().values().stream()
                .filter(cfg -> cfg.key().equals(key))
                .findFirst()
                .map(cfg -> cfg.roles().stream()
                        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                        .toList()
                )
                .orElse(List.of());
    }
}
