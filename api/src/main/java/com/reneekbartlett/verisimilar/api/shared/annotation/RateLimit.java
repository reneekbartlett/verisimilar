package com.reneekbartlett.verisimilar.api.shared.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * Rate limiting threshold (QPS), default 5 per second
     */
    double qps() default 5.0;

    /**
     * Token acquisition strategy
     * true: Blocking mode (wait until token is acquired or timeout)
     * false: Non-blocking mode (fail immediately if token is not available)
     */
    boolean block() default true;

    /**
     * Timeout for blocking wait (only effective when block=true)
     * Default 0, indicating infinite wait
     */
    long timeout() default 0;

    /**
     * Timeout unit
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * Warm-up time
     * Default 0 (SmoothBursty); set >0 to enable warming-up mode (SmoothWarmingUp)
     */
    long warmupPeriod() default 0;

    /**
     * Warm-up time unit
     */
    TimeUnit warmupUnit() default TimeUnit.SECONDS;

    /**
     * Rate limiting prompt message
     */
    String message() default "System is busy, please try again later";
}
