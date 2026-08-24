package com.reneekbartlett.verisimilar.core.datasets.key;

import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

public interface DatasetKey {
    String toString();
    String id();
    Set<TemplateField> fields();
}
