package com.reneekbartlett.verisimilar.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;

import com.reneekbartlett.verisimilar.api.config.TestConfig;
import com.reneekbartlett.verisimilar.api.service.ExternalApiClient;

@SpringBootTest(
        classes = { TestConfig.class, ExternalApiClient.class},
        properties = { "api.external.base-url=http://localhost:8089", "spring.aop.proxy-target-class=true" }
)
@WireMockTest(httpPort = 8089)
public class ApiRetryIntegrationTest {

    @Autowired
    private ExternalApiClient apiClient; // Your AOP-annotated client bean

    @Test
    public void testRetryOn429RateLimitSuccess(WireMockRuntimeInfo wmRuntimeInfo) {
        String testUrl = "http://localhost:8089/api/generate/person";
        //String testUrl = "/api/generate/person";

        // Hit Bucket4j rate limit (429)
        stubFor(get(urlEqualTo("/api/generate/person"))
            .inScenario("Bucket4j Rate Limiting")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse()
                .withStatus(429)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"error\": \"Too Many Requests\"}"))
            .willSetStateTo("First Retry"));

        // Still hits (429)
        stubFor(get(urlEqualTo("/api/generate/person"))
            .inScenario("Bucket4j Rate Limiting")
            .whenScenarioStateIs("First Retry")
            .willReturn(aResponse()
                .withStatus(429)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"error\": \"Too Many Requests\"}"))
            .willSetStateTo("Second Retry"));

        // Bucket has refilled, attempt succeeds (200)
        stubFor(get(urlEqualTo("/api/generate/person"))
            .inScenario("Bucket4j Rate Limiting")
            .whenScenarioStateIs("Second Retry")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"data\": \"Success after retries!\"}")));

        // Execute call. AOP proxy will catch the two 429s, back off, and return the 3rd attempt.
        // callRemoteApi has @Retryable annotation with maxRetries/delay/multiplier
        String response = apiClient.callRemoteApi(testUrl);

        // Assert that the final result is the successful 3rd call
        assertEquals("{\"data\": \"Success after retries!\"}", response);

        // Verify WireMock was hit exactly 3 times total
        verify(3, getRequestedFor(urlEqualTo("/api/generate/person")));
    }

    @Test
    public void testRetryExhaustionWhenApiStaysRateLimitedIndefinitely(WireMockRuntimeInfo wmRuntimeInfo) {

        // Diagnostic check: This should NOT print "com.example.api.client.ExternalApiClient"
        System.out.println("CLIENT CLASS TYPE: " + apiClient.getClass().getName());

        String testPath = "/api/generate/person";
        String testUrl = "http://localhost:8089/api/generate/person";

        // Configure WireMock to respond with a 429 status code for EVERY single request indefinitely
        stubFor(get(urlEqualTo(testPath))
            .willReturn(aResponse()
                .withStatus(429)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"error\": \"Too Many Requests - Rate Limit Exceeded\"}")));

        // Verify that the client eventually throws the exception after exhausting its retries
        HttpClientErrorException.TooManyRequests thrownException = assertThrows(
            HttpClientErrorException.TooManyRequests.class,
            () -> apiClient.callRemoteApi(testUrl),
            "Expected callRemoteApi to throw a 429 TooManyRequests exception after retries exhausted"
        );

        // Assert that the thrown exception contains the correct HTTP status code
        assertEquals(429, thrownException.getStatusCode().value());

        // Verify that the AOP proxy attempted exactly 4 calls in total (1 initial try + 3 retries)
        verify(4, getRequestedFor(urlEqualTo(testPath)));
    }
}
