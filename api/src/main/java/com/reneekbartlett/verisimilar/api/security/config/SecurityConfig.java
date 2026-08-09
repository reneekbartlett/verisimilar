package com.reneekbartlett.verisimilar.api.security.config;

import com.reneekbartlett.verisimilar.api.security.filter.ApiKeyAuthFilter;
import com.reneekbartlett.verisimilar.api.security.filter.JwtAuthFilter;
import com.reneekbartlett.verisimilar.api.security.ApiKeyAuthProvider;
import com.reneekbartlett.verisimilar.api.security.ApiKeyProperties;
import com.reneekbartlett.verisimilar.api.security.JwtAuthEntryPoint;
import com.reneekbartlett.verisimilar.api.security.JwtAuthProvider;
//import com.reneekbartlett.verisimilar.api.filter.RequestLoggingFilter;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private final ApiKeyProperties apiKeyproperties;

    private final ApiKeyAuthProvider apiKeyAuthProvider;

    private final JwtAuthProvider jwtAuthProvider;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(
            ApiKeyProperties apiKeyproperties,
            ApiKeyAuthProvider apiKeyAuthProvider,
            JwtAuthProvider jwtAuthProvider,
            JwtAuthEntryPoint jwtAuthEntryPoint
    ) {
        this.apiKeyAuthProvider = apiKeyAuthProvider;
        this.apiKeyproperties = apiKeyproperties;

        this.jwtAuthProvider = jwtAuthProvider;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    //@Bean
    //public RequestLoggingFilter loggingFilter() {
    //    return new RequestLoggingFilter();
    //}

    @Bean
    public AuthenticationManager authenticationManager() {
        // The order in the List determines the order Spring Security checks them
        return new ProviderManager(List.of(apiKeyAuthProvider, jwtAuthProvider));
    }

    //@Bean
    //public ApiKeyAuthFilter apiKeyAuthFilter(AuthenticationManager authenticationManager) {
        //AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        //authManagerBuilder.authenticationProvider(apiKeyAuthProvider);
    //    return new ApiKeyAuthFilter(authenticationManager, apiKeyproperties);
    //}

    //@Bean
    //public JwtAuthFilter jwtAuthFilter(AuthenticationManager authenticationManager) throws Exception {
        //AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        //authManagerBuilder.authenticationProvider(jwtAuthProvider);
    //    return new JwtAuthFilter(authenticationManager);
    //}

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager
    ) throws Exception {
        ApiKeyAuthFilter apiKeyAuthFilter = new ApiKeyAuthFilter(authenticationManager, apiKeyproperties);
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(authenticationManager);

        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 1. Place LoggingFilter at the very beginning
                // .addFilterBefore(loggingFilter(), LogoutFilter.class)
                // 2. Place ApiKeyAuthFilter before general authentication
                .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/favicon.ico", "/css/**", "/images/**", "/js/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/generate/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthEntryPoint)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Access Denied\"}");
                        })
                );

        // Chain the filters in an OR architecture sequence
        //http.addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
