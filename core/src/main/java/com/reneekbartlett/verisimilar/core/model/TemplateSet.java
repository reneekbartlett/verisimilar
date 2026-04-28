package com.reneekbartlett.verisimilar.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record TemplateSet(Set<String> templates) {
    public static TemplateSet of(String... values) {
        return new TemplateSet(Set.of(values));
    }

//    public static TemplateSet defaults() {
//        return TemplateSet.of(
//                "${KEYWORD}.${NUM10}",
//                "${KEYWORD}${NUM10}",
//                "${KEYWORD}${NUM10}0",
//                "${KEYWORD}${NUM10}00"
//        );
//    }

    public List<String> toList(){
        return new ArrayList<String>(templates);
    }

    public static TemplateSet combine(TemplateSet set1, TemplateSet set2) {
        Set<String> combined = new HashSet<>();
        combined.addAll(set1.templates());
        combined.addAll(set2.templates());
        return new TemplateSet(combined);
    }
}
