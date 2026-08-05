package com.reneekbartlett.verisimilar.api.shared.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    String action() default ""; // e.g., "GENERATE_BULK", "ADD_FIRST_NAME", etc.
}
