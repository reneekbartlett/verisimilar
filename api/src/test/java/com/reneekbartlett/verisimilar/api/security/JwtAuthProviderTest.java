package com.reneekbartlett.verisimilar.api.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.reneekbartlett.verisimilar.api.security.service.JwtService;

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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isAuthenticated()).isTrue();
        Assertions.assertThat(result.getPrincipal()).isEqualTo("test_user");
        Assertions.assertThat(result.getCredentials()).isEqualTo(rawToken);

        // Verify authorities are converted correctly
        Assertions.assertThat(result.getAuthorities())
            .extracting(GrantedAuthority::getAuthority)
            .containsOnly("ROLE_USER", "ROLE_ADMIN");

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
        BadCredentialsException exception = Assertions
                .catchThrowableOfType(BadCredentialsException.class,() -> jwtAuthProvider.authenticate(unauthenticatedToken));

        // If needed, run your assertions on the caught object below
        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getMessage()).isEqualTo("The provided JSON Web Token is invalid or expired.");
        Assertions.assertThat(exception.getCause()).isInstanceOf(JOSEException.class);

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
        Assertions.assertThat(supportsJwtToken).isTrue();
    }

    @Test
    void supports_WithUnsupportedTokenClass_ReturnsFalse() {
        // Act & Assert
        // Standard Spring username/password mock authentication token class should fail validation
        boolean supportsOtherToken = jwtAuthProvider
                .supports(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class);
        Assertions.assertThat(supportsOtherToken).isFalse();
    }
}
