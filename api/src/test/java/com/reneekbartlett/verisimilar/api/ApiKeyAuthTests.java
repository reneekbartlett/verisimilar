package com.reneekbartlett.verisimilar.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthFilter;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthProvider;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthenticationToken;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyService;
import com.reneekbartlett.verisimilar.api.security.config.SecurityConfig;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(controllers = TestGeneratePersonController.class)
@Import({SecurityConfig.class, ApiKeyAuthFilter.class})
//@Disabled
public class ApiKeyAuthTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApiKeyService apiKeyService;

    @MockitoBean
    private ApiKeyAuthProvider apiKeyAuthProvider;

    @BeforeEach
    void setup() {
        when(apiKeyService.isValid("VALID_KEY")).thenReturn(true);
        when(apiKeyService.isValid("BAD_KEY")).thenReturn(false);
        when(apiKeyService.getClientId("VALID_KEY")).thenReturn(java.util.Optional.of("test_user"));
        when(apiKeyService.getAuthorities("VALID_KEY")).thenReturn(List.of());
        when(apiKeyAuthProvider.supports(ApiKeyAuthenticationToken.class)).thenReturn(true);
        when(apiKeyAuthProvider.authenticate(any()))
                .thenAnswer(invocation -> {
                    ApiKeyAuthenticationToken token = invocation.getArgument(0);
                    return new ApiKeyAuthenticationToken(token.getCredentials().toString(),"test_user", List.of(), true);
                });
    }

    @Test
    //@Disabled
    void validApiKey_allowsAccess() throws Exception {
        mockMvc.perform(get("/api-test/generate/person").header("X-API-Key", "VALID_KEY")).andExpect(status().isOk());
    }

    //@Test
    //@Disabled
    void missingApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/generate/person")).andExpect(status().isUnauthorized());
    }

    //@Test
    //@Disabled
    void invalidApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/generate/person").header("X-API-Key", "BAD_KEY")).andExpect(status().isUnauthorized());
    }

    //@Test
    //@Disabled
    void publicEndpoint_doesNotRequireApiKey() throws Exception {
        mockMvc.perform(get("/public/status")).andExpect(status().isOk());
    }
}
