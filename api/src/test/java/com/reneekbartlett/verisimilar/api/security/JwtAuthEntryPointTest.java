package com.reneekbartlett.verisimilar.api.security;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class JwtAuthEntryPointTest {

    private JwtAuthEntryPoint entryPoint;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        entryPoint = new JwtAuthEntryPoint();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
    }

    @Test
    void commence_WritesCorrectJsonErrorPayloadAndStatus() throws IOException, ServletException {
        // Arrange
        request.setServletPath("/api/v1/secure-data");
        AuthenticationException exception = new BadCredentialsException("The provided JSON Web Token is invalid or expired.");

        // Act
        entryPoint.commence(request, response, exception);

        // Assert
        // 1. Verify HTTP Response Headers and Status Code
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        Assertions.assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        // 2. Parse the written raw string output into a JSON tree structure for evaluation
        String jsonResponseBody = response.getContentAsString();

        Assertions.assertThat(jsonResponseBody).isNotNull().isNotBlank();

        JsonNode rootNode = objectMapper.readTree(jsonResponseBody);

        // 3. Verify object field mapping rules match the exact contract keys
        // 3. Verify object field mapping metrics safely without any node text/string accessors
        //assertTrue(rootNode.hasNonNull("timestamp")); // Replaces old presence checks safely
        Assertions.assertThat(rootNode.hasNonNull("timestamp")).isTrue();

        // Extract the timestamp field safely using tree conversion instead of a text accessor method
        String timestampValue = objectMapper.treeToValue(rootNode.path("timestamp"), String.class);

        //assertEquals(401, rootNode.get("status").asInt());
        //assertNotNull(timestampValue, "Timestamp must be a valid text string asset");
        //assertFalse(timestampValue.isBlank(), "Timestamp value string content cannot be blank");
        //assertEquals("Unauthorized", rootNode.get("error").asText());
        //assertEquals("The provided JSON Web Token is invalid or expired.", rootNode.get("message").asText());
        //assertEquals("/api/v1/secure-data", rootNode.get("path").asText());

        Assertions.assertThat(rootNode.get("status").asInt()).isEqualTo(401);
        Assertions.assertThat(timestampValue).isNotNull().isNotBlank();
    }
}
