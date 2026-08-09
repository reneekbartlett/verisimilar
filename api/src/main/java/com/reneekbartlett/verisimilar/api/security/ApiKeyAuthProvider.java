package com.reneekbartlett.verisimilar.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.api.security.service.ApiKeyService;

@Component
public class ApiKeyAuthProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyAuthProvider.class);

    private final ApiKeyService apiKeyService;

    public ApiKeyAuthProvider(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Get Key and Validate
        String key;
        try {
            key = (String) authentication.getCredentials();
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid key");
        }

        if (!apiKeyService.isValid(key)) {
            throw new BadCredentialsException("Invalid API Key");
        }
        
        LOGGER.debug("{} is valid", key);

        // Resolve client ID
        String clientId = apiKeyService.getClientId(key).orElse("unknown-client");

        // Resolve authorities (roles)
        var authorities = apiKeyService.getAuthorities(key);

        // Return authenticated token
        return new ApiKeyAuthToken(
                key,
                clientId,
                authorities,
                true
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthToken.class.isAssignableFrom(authentication);
    }
}
