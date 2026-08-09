package com.reneekbartlett.verisimilar.api.security;

import com.reneekbartlett.verisimilar.api.security.service.JwtService;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.JOSEException;

@Component
public class JwtAuthProvider implements AuthenticationProvider {

    private final JwtService jwtService;
    //private final UserDetailsService userDetailsService;

    public JwtAuthProvider(JwtService jwtService) {
        this.jwtService = jwtService;
        //this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String rawToken = (String) authentication.getCredentials();

        String username;
        List<String> roles;
        //UserDetails userDetails;
        List<SimpleGrantedAuthority> authorities;
        try {
            JWTClaimsSet claims = jwtService.extractClaims(rawToken);
            username = jwtService.extractUsername(claims);
            roles = jwtService.extractRoles(claims);

            authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            return new JwtAuthToken(username, rawToken, authorities);

        //} catch (JwtException | IllegalArgumentException e) {
        //    throw new BadCredentialsException("The provided JSON Web Token is invalid or expired.", e);
        } catch(JOSEException | ParseException e) {
            throw new BadCredentialsException("The provided JSON Web Token is invalid or expired.", e);
        }
    }

    //private JwtAuthToken getJwtAuthToken(String username, String rawToken, List<String> roles) {
    //    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    //    return new JwtAuthToken(userDetails, rawToken, userDetails.getAuthorities());
    //}

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.isAssignableFrom(authentication);
    }
}
