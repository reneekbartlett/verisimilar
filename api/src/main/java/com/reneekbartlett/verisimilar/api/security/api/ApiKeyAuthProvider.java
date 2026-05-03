package com.reneekbartlett.verisimilar.api.security.api;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthProvider implements AuthenticationProvider {

    private final ApiKeyService apiKeyService;

    public ApiKeyAuthProvider(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        // Get Key and Validate
        String key = (String) authentication.getCredentials();
        if (!apiKeyService.isValid(key)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        // Resolve client ID
        String clientId = apiKeyService.getClientId(key).orElse("unknown-client");

        // Resolve authorities (roles)
        var authorities = apiKeyService.getAuthorities(key);

        // Return authenticated token
        return new ApiKeyAuthenticationToken(
                key,
                clientId,
                authorities,
                true
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
