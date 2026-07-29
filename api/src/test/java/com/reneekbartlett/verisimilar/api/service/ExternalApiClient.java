package com.reneekbartlett.verisimilar.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.resilience.annotation.Retryable; // Spring Boot 4 Native
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalApiClient {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalApiClient.class);

    private final RestTemplate restTemplate;
    @SuppressWarnings("unused")
    private final String apiBaseUrl;

    public ExternalApiClient(RestTemplate restTemplate, @Value("${api.external.base-url:http://localhost:8089}") String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * Calls the remote API resource. 
     * If the server returns a 429 status code, Spring Boot 4 AOP intercepts 
     * the error and performs an exponential backoff retry loop.
     */
    @Retryable(
        includes = HttpClientErrorException.TooManyRequests.class, // Triggers on 429 errors
        maxRetries = 3,          // 3 retry attempts after the initial failure (4 calls total)
        delay = 1000,            // Base wait time of 1000ms (kept low for faster test suites)
        multiplier = 2.0,        // Exponential backoff curve: 1s -> 2s -> 4s
        maxDelay = 5000,         // Caps the absolute longest wait duration at 5 seconds
        jitter = (long)(0.25)    // Adds up to 25% random variance to the sleep time
    )
    public String callRemoteApi(String path) {
        //URI targetUri = UriComponentsBuilder.fromPath(apiBaseUrl).path(path).build().toUri();
        return restTemplate.getForObject(path, String.class);
    }
}
