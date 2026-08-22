package com.reneekbartlett.verisimilar.api.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.reneekbartlett.verisimilar.api.security.JwtAuthToken;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private FilterChain filterChain;

    private JwtAuthFilter jwtAuthFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter(authenticationManager);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext(); // Ensure clean slate before each test
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext(); // Keep static context clean after execution
    }

    @Test
    void doFilterInternal_WithValidBearerToken_AuthenticatesAndSavesToContext() throws ServletException, IOException {
        // Arrange
        String rawToken = "valid.jwt.payload";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + rawToken);

        // Prepare the authenticated result token that the manager would return
        Authentication authenticatedResult = new JwtAuthToken("john_doe", rawToken, List.of());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authenticatedResult);

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        // 1. Verify the manager was invoked with the extracted credentials
        ArgumentCaptor<Authentication> authCaptor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(authCaptor.capture());
        Assertions.assertThat(authCaptor.getValue().getCredentials()).isEqualTo(rawToken);

        // 2. Verify security context holds the authentication payload
        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertThat(contextAuth).isNotNull();
        Assertions.assertThat(contextAuth.getPrincipal()).isEqualTo("john_doe");

        // 3. Verify filter chain execution successfully continued to the next filter
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithMissingAuthorizationHeader_SkipsAuthenticationAndContinuesChain() throws ServletException, IOException {
        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        // Filter should bypass token parsing if the header isn't present
        verify(authenticationManager, never()).authenticate(any());
        Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidHeaderPrefix_SkipsAuthenticationAndContinuesChain() throws ServletException, IOException {
        // Arrange
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNz"); // Not 'Bearer '

        // Act
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(authenticationManager, never()).authenticate(any());
        Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_ClearsContextAndThrowsException() throws ServletException, IOException {
        // Arrange
        String badToken = "expired.or.bad.token";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + badToken);

        // Populate context initially to verify that the filter wipes it clean on failure
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("The provided JSON Web Token is invalid or expired."));

        // Act & Assert
        assertThatThrownBy(() -> jwtAuthFilter.doFilterInternal(request, response, filterChain))
            .isInstanceOf(BadCredentialsException.class)
            .hasMessageContaining("The provided JSON Web Token is invalid or expired.");

        // Context must be nullified to prevent stale/incorrect sessions
        Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // Chain should abort immediately—never continuing down to controllers
        verify(filterChain, never()).doFilter(request, response);
    }
}
