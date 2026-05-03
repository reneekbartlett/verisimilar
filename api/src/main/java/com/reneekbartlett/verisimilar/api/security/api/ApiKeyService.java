package com.reneekbartlett.verisimilar.api.security.api;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiKeyService {

    private final ApiKeyProperties properties;

    public ApiKeyService(ApiKeyProperties properties) {
        this.properties = properties;
    }

    public boolean isValid(String key) {
        return properties.getApiKeys().values().stream()
                .anyMatch(cfg -> cfg.getKey().equals(key));
    }

    public Optional<String> getClientId(String key) {
        return properties.getApiKeys().entrySet().stream()
                .filter(e -> e.getValue().getKey().equals(key))
                .map(e -> e.getKey())
                .findFirst();
    }

    public List<GrantedAuthority> getAuthorities(String key) {
        return properties.getApiKeys().values().stream()
                .filter(cfg -> cfg.getKey().equals(key))
                .findFirst()
                .map(cfg -> cfg.getRoles().stream()
                        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                        .toList()
                )
                .orElse(List.of());
    }
}
