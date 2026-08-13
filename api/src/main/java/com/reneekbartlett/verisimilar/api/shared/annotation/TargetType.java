package com.reneekbartlett.verisimilar.api.shared.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetType {
    String value(); // This holds your target type value (e.g., "STRING", "DATE", "UUID")
}
