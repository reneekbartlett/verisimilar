package com.reneekbartlett.verisimilar.core.model;

import java.util.Set;

public record TemplateSet(Set<String> templates) {
    public static TemplateSet of(String... values) {
        return new TemplateSet(Set.of(values));
    }

    public static TemplateSet defaults() {
        return TemplateSet.of(
                "${KEYWORD}.${NUM10}",
                "${KEYWORD}${NUM10}",
                "${KEYWORD}${NUM10}0",
                "${KEYWORD}${NUM10}00"
        );
    }
}
