package com.reneekbartlett.verisimilar.api.shared.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalService {
    String apiName() default "Generic-API";
    int maxRetries() default 3; // Basic retry handling
}
