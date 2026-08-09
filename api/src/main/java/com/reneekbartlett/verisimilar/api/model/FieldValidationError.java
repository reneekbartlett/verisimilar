package com.reneekbartlett.verisimilar.api.model;

public record FieldValidationError(
        String field, 
        String rejectedValue, 
        String message
) implements ApiSubError {}
