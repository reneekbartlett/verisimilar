package com.reneekbartlett.verisimilar.api.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtAuthToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 8187754587085827438L;

    private final String encryptedToken;
    private final Object principal;

    // Unauthenticated constructor
    public JwtAuthToken(String encryptedToken) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.encryptedToken = encryptedToken;
        this.principal = null;
        super.setAuthenticated(false);
    }

    // Authenticated constructor
    public JwtAuthToken(Object principal, String encryptedToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.encryptedToken = encryptedToken;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.encryptedToken;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
