package com.reneekbartlett.verisimilar.api.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.JOSEException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.reneekbartlett.verisimilar.api.security.service.JwtService;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthProviderTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private JWTClaimsSet mockClaims;

    private JwtAuthProvider jwtAuthProvider;

    @BeforeEach
    void setUp() {
        // Initialize provider manually with the mocked service dependency
        jwtAuthProvider = new JwtAuthProvider(jwtService);
    }

    @Test
    void authenticate_WithValidToken_ReturnsAuthenticatedToken() throws JOSEException, ParseException {
        // Arrange
        String rawToken = "valid.jwt.token";
        JwtAuthToken unauthenticatedToken = new JwtAuthToken(rawToken);

        when(jwtService.extractClaims(rawToken)).thenReturn(mockClaims);
        when(jwtService.extractUsername(mockClaims)).thenReturn("test_user");
        when(jwtService.extractRoles(mockClaims)).thenReturn(List.of("ROLE_USER", "ROLE_ADMIN"));

        // Act
        Authentication result = jwtAuthProvider.authenticate(unauthenticatedToken);

        // Assert
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals("test_user", result.getPrincipal());
        assertEquals(rawToken, result.getCredentials());

        // Verify authorities are converted correctly
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertEquals(2, result.getAuthorities().size());

        //verify(jwtService).validateAndExtractClaims(rawToken);
        verify(jwtService).extractClaims(rawToken);
        verify(jwtService).extractUsername(mockClaims);
        verify(jwtService).extractRoles(mockClaims);
    }

    @Test
    void authenticate_WithInvalidOrExpiredToken_ThrowsBadCredentialsException() throws JOSEException, ParseException {
        // Arrange
        String invalidToken = "malformed.or.expired.token";
        JwtAuthToken unauthenticatedToken = new JwtAuthToken(invalidToken);

        when(jwtService.extractClaims(invalidToken))
            .thenThrow(new JOSEException("Signature validation failed"));

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            jwtAuthProvider.authenticate(unauthenticatedToken);
        });

        assertEquals("The provided JSON Web Token is invalid or expired.", exception.getMessage());
        assertInstanceOf(JOSEException.class, exception.getCause()); // Verifies the root cause is retained

        // Verify execution stopped immediately after validation failed
        //verify(jwtService).validateAndExtractClaims(invalidToken);
        verify(jwtService).extractClaims(invalidToken);

        verify(jwtService, never()).extractUsername(any());
        verify(jwtService, never()).extractRoles(any());
    }

    @Test
    void supports_WithJwtAuthTokenClass_ReturnsTrue() {
        // Act & Assert
        boolean supportsJwtToken = jwtAuthProvider.supports(JwtAuthToken.class);
        assertTrue(supportsJwtToken);
    }

    @Test
    void supports_WithUnsupportedTokenClass_ReturnsFalse() {
        // Act & Assert
        // Standard Spring username/password mock authentication token class should fail validation
        boolean supportsOtherToken = jwtAuthProvider
                .supports(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class);
        assertFalse(supportsOtherToken);
    }
}
