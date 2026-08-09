package com.reneekbartlett.verisimilar.api.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class ApiKeyAuthToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 8469795932450057480L;
    private final String apiKey;
    private final String principal;

    public ApiKeyAuthToken(String apiKey) {
        super(AuthorityUtils.NO_AUTHORITIES); // non-null
        this.apiKey = apiKey;
        this.principal = null;
        setAuthenticated(false);
    }

    // Authenticated constructor (after provider validates key)
    public ApiKeyAuthToken(
            String apiKey,
            String principal,
            Collection<? extends GrantedAuthority> authorities,
            boolean authenticated
    ) {
        super(authorities == null ? AuthorityUtils.NO_AUTHORITIES : authorities);
        this.apiKey = apiKey;
        this.principal = principal;
        setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
