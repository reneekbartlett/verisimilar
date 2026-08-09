package com.reneekbartlett.verisimilar.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.reneekbartlett.verisimilar.api.controller.GeneratePersonController;
import com.reneekbartlett.verisimilar.api.dto.PersonResponseDto;
import com.reneekbartlett.verisimilar.api.security.ApiKeyAuthProvider;
import com.reneekbartlett.verisimilar.api.security.ApiKeyAuthToken;
import com.reneekbartlett.verisimilar.api.security.ApiKeyProperties;
import com.reneekbartlett.verisimilar.api.security.JwtAuthEntryPoint;
import com.reneekbartlett.verisimilar.api.security.JwtAuthProvider;
import com.reneekbartlett.verisimilar.api.security.config.SecurityConfig;
import com.reneekbartlett.verisimilar.api.security.service.ApiKeyService;
import com.reneekbartlett.verisimilar.api.service.GeneratePersonService;

/***
 * Components:  ApiKeyProperties, ApiKeyAuthProvider
 * Services:    ApiKeyService
 */

@WebMvcTest(
    controllers = { GeneratePersonController.class }, 
    properties = { 
        "application.security.shared-secret=MySecretPassphraseMustBe32Bytes!",
        "application.security.allowUrlApiKeys=true",
        "application.security.apiUsers=test-user,swagger",
        "application.security.apiKeys.swagger.key=SwaggerKey123",
        "application.security.apiKeys.swagger.roles=GENERATE",
        "application.security.apiKeys.test-user.key=VALID_KEY",
        "application.security.apiKeys.test-user.roles=GENERATE"
    }
)
@Import({
    SecurityConfig.class, ApiKeyProperties.class, ApiKeyAuthProvider.class, ApiKeyService.class
})
// TODO:  https://docs.spring.io/spring-security/reference/servlet/test/method.html
//@org.junit.jupiter.api.Disabled
public class AuthenticationIntegrationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationIntegrationTests.class);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    public JwtAuthProvider jwtAuthProvider;

    @MockitoBean
    public JwtAuthEntryPoint jwtAuthEntryPoint;

    @MockitoBean
    public GeneratePersonService generateService;

    @BeforeEach
    void setup() {
        // ApiKeyService(ApiKeyProperties properties)
        //when(apiKeyService.isValid("VALID_KEY")).thenReturn(true);
        //when(apiKeyService.isValid("BAD_KEY")).thenReturn(false);
        //when(apiKeyService.getClientId("VALID_KEY")).thenReturn(java.util.Optional.of("test_user"));

        //List<GrantedAuthority> mockAuthorityList = List.of((GrantedAuthority)new SimpleGrantedAuthority("ROLE_GENERATE"));
        //when(apiKeyService.getAuthorities("VALID_KEY")).thenReturn(mockAuthorityList);

        // ApiKeyAuthProvider(ApiKeyService apiKeyService)
        //when(apiKeyAuthProvider.supports(ApiKeyAuthToken.class)).thenReturn(true);
        //when(apiKeyAuthProvider.authenticate(any()))
        //        .thenAnswer(invocation -> {
        //            ApiKeyAuthToken token = invocation.getArgument(0);
        //            boolean isValid = this.apiKeyService.isValid(token.getCredentials().toString());
        //            LOGGER.debug("isValid={}", isValid);
        //            return new ApiKeyAuthToken(token.getCredentials().toString(),"test_user", mockAuthorityList, true);
        //        });

        //ApiKeyAuthenticationToken testToken = new ApiKeyAuthenticationToken("");

        //handlerMapping.getHandlerMethods().forEach((info, method) -> {
        //    LOGGER.debug("" + info.getDirectPaths() + " : " + method.getMethod().getName());
        //});
        LOGGER.debug("setup");
    }

    @Test
    public void validApiKey_allowsAccess() throws Exception {
        PersonResponseDto mockPersonDto = new PersonResponseDto();
        mockPersonDto.setUuid("TEST_UUID");
        mockPersonDto.setFirstName("JANE");
        mockPersonDto.setMiddleName("MARIE");
        mockPersonDto.setLastName("BARTLETT");
        when(generateService.generate(any())).thenReturn(mockPersonDto);

        LOGGER.debug("started");
        MvcResult mvcResult = mockMvc.perform(get("/api/generate/person?api_key=VALID_KEY").header("X-API-Key", "VALID_KEY"))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        LOGGER.debug("response: status={}; content={}", response.getStatus(), response.getContentAsString());
        LOGGER.debug("");
    }

    @Test
    public void missingApiKey_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/generate/person")).andExpect(status().isUnauthorized());
    }

    @Test
    public void invalidApiKey_returnsUnauthorized() throws Exception {
        // Broken down test case for example troubleshooting
        MockHttpServletRequestBuilder reqBldr = get("/api/generate/person").header("X-API-Key", "BAD_KEY");
        ResultMatcher resultMatcher = status().isUnauthorized();
        ResultActions resultActions = mockMvc.perform(reqBldr).andExpect(resultMatcher);
        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        LOGGER.debug("response: status={}; content={}", response.getStatus(), response.getContentAsString());
        LOGGER.debug("");
    }

    @Test
    public void publicEndpoint_doesNotRequireApiKey() throws Exception {
        mockMvc.perform(get("/api/public/status")).andExpect(status().isOk());
    }
}
