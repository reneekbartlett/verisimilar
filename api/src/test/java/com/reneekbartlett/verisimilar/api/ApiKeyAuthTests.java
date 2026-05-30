package com.reneekbartlett.verisimilar.api;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.GrantedAuthority;

import com.reneekbartlett.verisimilar.api.controller.GeneratePersonController;
import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthProvider;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthenticationToken;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyProperties;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyService;
import com.reneekbartlett.verisimilar.api.security.config.SecurityConfig;
import com.reneekbartlett.verisimilar.api.service.GeneratePersonService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;

//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.test.context.support.WithMockUser;

/***
 * Components:  ApiKeyProperties, ApiKeyAuthProvider
 * Services:    ApiKeyService
 */

@WebMvcTest(controllers = GeneratePersonController.class)
@Import({SecurityConfig.class, ApiKeyProperties.class})
// TODO:  https://docs.spring.io/spring-security/reference/servlet/test/method.html
//@Disabled
public class ApiKeyAuthTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyAuthTests.class);

    //@Autowired
    //public RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    public ApiKeyProperties apiKeyProperties;

    @MockitoBean
    public ApiKeyService apiKeyService;

    @MockitoBean
    public ApiKeyAuthProvider apiKeyAuthProvider;

    @MockitoBean
    public GeneratePersonService generateService;

    @BeforeEach
    void setup() {
//        Map<String, ApiKeyConfig> apiKeys = HashMap.newHashMap(1);
//        ApiKeyConfig cfg = new ApiKeyConfig();
//        cfg.setKey("VALID_KEY");
//        cfg.setRoles(List.of("GENERATE"));
//        apiKeys.put("test_user", cfg);
//        apiKeyProperties.setApiKeys(apiKeys);
//        apiKeyProperties.setAllowUrlApiKeys(true);
//
//        when(apiKeyProperties.isAllowUrlApiKeys()).thenReturn(true);
//        when(apiKeyProperties.getApiKeys()).thenReturn(apiKeys);

        // ApiKeyService(ApiKeyProperties properties)
        when(apiKeyService.isValid("VALID_KEY")).thenReturn(true);
        when(apiKeyService.isValid("BAD_KEY")).thenReturn(false);
        when(apiKeyService.getClientId("VALID_KEY")).thenReturn(java.util.Optional.of("test_user"));

        List<GrantedAuthority> mockAuthorityList = List.of((GrantedAuthority)new SimpleGrantedAuthority("ROLE_GENERATE"));
        when(apiKeyService.getAuthorities("VALID_KEY")).thenReturn(mockAuthorityList);

        // ApiKeyAuthProvider(ApiKeyService apiKeyService)
        when(apiKeyAuthProvider.supports(ApiKeyAuthenticationToken.class)).thenReturn(true);
        when(apiKeyAuthProvider.authenticate(any()))
                .thenAnswer(invocation -> {
                    ApiKeyAuthenticationToken token = invocation.getArgument(0);
                    return new ApiKeyAuthenticationToken(token.getCredentials().toString(),"test_user", mockAuthorityList, true);
                });

        //ApiKeyAuthenticationToken testToken = new ApiKeyAuthenticationToken("");

        //handlerMapping.getHandlerMethods().forEach((info, method) -> {
        //    LOGGER.debug("" + info.getDirectPaths() + " : " + method.getMethod().getName());
        //});
        LOGGER.debug("setup");
    }

    @Test
    @WithMockUser(username = "test_user", roles = {"GENERATE"})
    void validApiKey_allowsAccess() throws Exception {
        PersonResponseDto mockPersonDto = new PersonResponseDto();
        when(generateService.generate(any())).thenReturn(mockPersonDto);

        LOGGER.debug("started");
        mockMvc.perform(get("/api/generate/person?api_key=VALID_KEY")
                .header("X-API-Key", "VALID_KEY"))
                .andExpect(status().isOk());

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
        mockMvc.perform(get("/api/public/status")).andExpect(status().isOk());
    }
}
