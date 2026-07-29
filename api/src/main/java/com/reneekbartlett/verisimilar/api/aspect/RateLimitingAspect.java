package com.reneekbartlett.verisimilar.api.aspect;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.reneekbartlett.verisimilar.api.exception.RateLimitExceededException;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimited;
import com.reneekbartlett.verisimilar.api.util.LoggingUtils;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.TimeMeter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class RateLimitingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitingAspect.class);

    // Cache structure: Map<"IP:MethodName", Bucket>
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // Default to system clock for production runtime
    @SuppressWarnings("unused")
    private TimeMeter timeMeter = TimeMeter.SYSTEM_MILLISECONDS;

    // Package-private setter allowing unit tests to inject a controllable clock
    protected void setTimeMeter(TimeMeter timeMeter) {
        this.timeMeter = timeMeter;
        this.cache.clear(); // Clear cache to apply the new time meter
    }

    @Before("@annotation(rateLimited)")
    public void checkRateLimit(JoinPoint joinPoint, RateLimited rateLimited) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;

        HttpServletRequest httpRequest = attributes.getRequest();
        HttpServletResponse httpResponse = attributes.getResponse();

        // Resolve client IP
        // TODO:  Use local IP for testing
        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = httpRequest.getRemoteAddr();
        }

        LOGGER.debug("httpRequest parameters={}; headers=[{}]", LoggingUtils.buildHeadersString(httpRequest));

        // 2. Generate a unique key combination for IP + Method signature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.toShortString();
        String cacheKey = ip + ":" + methodName;

        // 3. Fetch or compute the bucket for this specific endpoint configuration
        Bucket bucket = cache.computeIfAbsent(cacheKey, k -> Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(rateLimited.capacity())
                        .refillIntervally(rateLimited.capacity(), Duration.ofSeconds(rateLimited.durationSeconds()))
                        .build())
                .build());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            if(httpResponse != null) {
                httpResponse.addHeader("X-RateLimit-Limit", String.valueOf(rateLimited.capacity()));
                httpResponse.addHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
                LOGGER.debug("httpResponse headers=[{}]", LoggingUtils.buildHeadersString(httpResponse));
            }
        } else {
            // No tokens left; reject the request with HTTP 429; Attach details plus the
            // mandatory wait time for a retry
            long waitForRefillSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
            long waitForResetSeconds = probe.getNanosToWaitForReset() / 1_000_000_000;

            // Throw exception to be handled globally, i.e. set HttpStatus, add rate limiting headers
            // RateLimitExceededException(long retryAfterSeconds, long capacity, long resetSeconds)
            throw new RateLimitExceededException(
                    waitForRefillSeconds <= 0 ? 1 : waitForRefillSeconds, 
                    rateLimited.capacity(), 
                    waitForResetSeconds);
        }
    }
}
