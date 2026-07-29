package com.reneekbartlett.verisimilar.api.aspect;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import com.reneekbartlett.verisimilar.api.exception.RateLimitException;
import com.reneekbartlett.verisimilar.api.shared.annotation.RateLimit;

@Slf4j
@Aspect
@Component
public class RateLimitAop {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitAop.class);

    private final Map<String, RateLimiter> rateLimiterCache = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.example.annotation.RateLimit)")
    public void rateLimitPointcut() {}

    @Around("rateLimitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        String methodKey = buildMethodKey(method); // Prevent method overloading

        RateLimiter rateLimiter = rateLimiterCache.computeIfAbsent(methodKey, key -> createRateLimiter(rateLimit));

        boolean acquireSuccess;
        if (rateLimit.block()) {
            if (rateLimit.timeout() <= 0) {
                // Infinite wait until success
                rateLimiter.acquire();
                acquireSuccess = true;
            } else {
                acquireSuccess = rateLimiter.tryAcquire(rateLimit.timeout(), rateLimit.timeUnit());
            }
        } else {
            // Try immediately, return on failure
            acquireSuccess = rateLimiter.tryAcquire();
        }

        if (!acquireSuccess) {
            LOGGER.warn("[ALERT! Rate Limit] Method {} too fast", methodKey);
            throw new RateLimitException(rateLimit.message());
        }

        // Release
        return joinPoint.proceed();
    }

    private String buildMethodKey(Method method) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(method.getDeclaringClass().getName())
                .append(".").append(method.getName()).append("(");

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            keyBuilder.append(parameterTypes[i].getSimpleName());
            if (i < parameterTypes.length - 1) {
                keyBuilder.append(",");
            }
        }
        keyBuilder.append(")");
        return keyBuilder.toString();
    }

    /**
     * Create a specific RateLimiter based on configuration
     */
    private RateLimiter createRateLimiter(RateLimit rateLimit) {
        if (rateLimit.warmupPeriod() > 0) {
            LOGGER.info("Creating warming-up rate limiter: QPS={}, Warmup={}s", rateLimit.qps(), rateLimit.warmupPeriod());
            return RateLimiter.create(rateLimit.qps(), rateLimit.warmupPeriod(), rateLimit.warmupUnit());
        } else {
            LOGGER.info("Creating standard rate limiter: QPS={}", rateLimit.qps());
            return RateLimiter.create(rateLimit.qps());
        }
    }
}
