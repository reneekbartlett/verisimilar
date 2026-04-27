package com.reneekbartlett.verisimilar.core.templates;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.TemplateSet;

/***
 * 
 */
public class TemplateRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateRegistry.class);

    private final Map<Set<TemplateField>, TemplateSet> registry;

    // defaultRegistry
    // Set<TemplateField> = HashSet.of("KEYWORD", "NUM10");
    private static final TemplateSet DEFAULT_KEYWORD_TEMPLATES = TemplateSet.defaults();

    public TemplateRegistry(Map<Set<TemplateField>, TemplateSet> registry) {
        this.registry = Map.copyOf(registry);
    }

    public Set<String> getTemplatesFor(Set<TemplateField> fields) {
        Set<String> stringTemplates = new LinkedHashSet<>();

        registry.forEach((key, value) -> {
            if (fields.containsAll(key)) {
                stringTemplates.addAll(value.templates());
            }
        });

        // TODO:  Move elsewhere?
        if(stringTemplates.size() == 0) {
            stringTemplates.addAll(DEFAULT_KEYWORD_TEMPLATES.templates());
            LOGGER.debug("Using default keyword templates");
        }

        return stringTemplates;
    }

    public static TemplateRegistry defaults() {
        Map<Set<TemplateField>, TemplateSet> registry = new HashMap<>();
        registry.put(Set.of(TemplateField.KEYWORD, TemplateField.NUM10), TemplateSet.defaults());
        return new TemplateRegistry(registry);
    }
}
