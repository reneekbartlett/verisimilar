package com.reneekbartlett.verisimilar.api.config;

import java.net.URI;

import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResilientMethods
public class TestConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate() {
            @Override
            public <T> @Nullable T getForObject(URI url, Class<T> responseType) throws RestClientException {
                // Custom logic or pre-processing here
                return super.getForObject(url, responseType);
            }
        };
    }
}
