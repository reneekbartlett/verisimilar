package com.reneekbartlett.verisimilar.api.security.config;

import com.reneekbartlett.verisimilar.api.filter.ApiKeyAuthFilter;
import com.reneekbartlett.verisimilar.api.filter.RequestLoggingFilter;
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
//import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private ApiKeyAuthProvider apiKeyAuthProvider;
    private final ApiKeyProperties properties;

    public SecurityConfig(ApiKeyAuthProvider apiKeyAuthProvider, ApiKeyProperties properties) {
        this.apiKeyAuthProvider = apiKeyAuthProvider;
        this.properties = properties;
    }

    @Bean
    public RequestLoggingFilter loggingFilter() {
        return new RequestLoggingFilter();
    }

    @Bean
    public ApiKeyAuthFilter authFilter() {
        AuthenticationManager authenticationManager = new ProviderManager(apiKeyAuthProvider);
        return new ApiKeyAuthFilter(authenticationManager, properties);
    }

    // TODO:  Add RateLimitService/RateLimitingFilter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            // 1. Place LoggingFilter at the very beginning
            //.addFilterBefore(loggingFilter(), LogoutFilter.class)
            // 2. Place ApiKeyAuthFilter before general authentication
            .addFilterBefore(
                    authFilter(),
                    UsernamePasswordAuthenticationFilter.class
            )
            //.addFilterAfter(new RateLimitingFilter(rateLimitService), ApiKeyAuthFilter.class);
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll() // status
                .requestMatchers("/api/generate/**").authenticated()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
