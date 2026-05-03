package com.reneekbartlett.verisimilar.api.security.config;

import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthFilter;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyAuthProvider;
import com.reneekbartlett.verisimilar.api.security.api.ApiKeyProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(ApiKeyAuthProvider apiKeyAuthProvider) {
        return new ProviderManager(apiKeyAuthProvider);
    }

    // TODO:  Add RateLimitService/RateLimitingFilter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            ApiKeyAuthProvider apiKeyAuthProvider,
            ApiKeyProperties properties
    ) throws Exception {
        var authManager = new ProviderManager(apiKeyAuthProvider);
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll() // status
                .requestMatchers("/api/generate/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new ApiKeyAuthFilter(authManager, properties),
                UsernamePasswordAuthenticationFilter.class
            );

            // TODO
            //.addFilterAfter(new RateLimitingFilter(rateLimitService),ApiKeyAuthFilter.class);
        return http.build();
    }
}
