package com.reneekbartlett.verisimilar.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import com.reneekbartlett.verisimilar.api.exception.ThirdPartyApiException;
import com.reneekbartlett.verisimilar.api.shared.annotation.ExternalService;

@Aspect
@Component
public class ThirdPartyApiHandlerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyApiHandlerAspect.class);

    @Around("@annotation(externalService)")
    public Object handleApiLifecycle(ProceedingJoinPoint joinPoint, ExternalService externalService) throws Throwable {
        String apiName = externalService.apiName();
        int maxAttempts = externalService.maxRetries();
        int attempt = 0;

        while (true) {
            try {
                attempt++;

                // Execute the actual HTTP request inside the service class
                return joinPoint.proceed();
            } catch (HttpStatusCodeException ex) { // Catches RestTemplate/RestClient native exceptions
                int statusCode = ex.getStatusCode().value();
                String responseBody = ex.getResponseBodyAsString();

                // Scenario A: Client Error (4xx) - Structural bug, do not retry!
                if (ex.getStatusCode().is4xxClientError()) {
                    LOGGER.error("[API-ERROR] Client error calling '{}'. Status: {}. Response: {}", 
                              apiName, statusCode, responseBody);
                    throw new ThirdPartyApiException("Invalid request sent to provider: " + apiName, statusCode, responseBody);
                }

                // Scenario B: Server Error (5xx) - Transient, evaluate retry policy
                if (ex.getStatusCode().is5xxServerError()) {
                    LOGGER.warn("[API-WARN] Server error on '{}'. Status: {}. Attempt {}/{}", 
                             apiName, statusCode, attempt, maxAttempts);

                    if (attempt >= maxAttempts) {
                        LOGGER.error("[API-FATAL] Max retries exhausted for external service '{}'.", apiName);
                        throw new ThirdPartyApiException("External provider down: " + apiName, statusCode, responseBody);
                    }

                    // Simple Exponential Backoff: Wait before trying again
                    Thread.sleep((long) Math.pow(2, attempt) * 500); 
                }

            } catch (Exception ex) {
                // Catch raw network dropouts (SocketTimeouts, ConnectExceptions)
                LOGGER.warn("[API-WARN] Network failure talking to '{}'. Attempt {}/{}", apiName, attempt, maxAttempts, ex);
                if (attempt >= maxAttempts) {
                    throw new ThirdPartyApiException("Network connection lost to provider: " + apiName, 503, ex.getMessage());
                }

                Thread.sleep(1000);
            }
        }
    }
}
