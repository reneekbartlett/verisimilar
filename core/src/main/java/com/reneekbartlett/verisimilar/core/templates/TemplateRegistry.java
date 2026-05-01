package com.reneekbartlett.verisimilar.core.templates;

import java.util.EnumSet;
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

    private final Map<EnumSet<TemplateField>, TemplateSet> registry;

    public TemplateRegistry(Map<EnumSet<TemplateField>, TemplateSet> registry) {
        this.registry = Map.copyOf(registry);
    }

    public Map<EnumSet<TemplateField>,TemplateSet> getTemplateMap(){
        return registry;
    }

    public Set<String> getTemplatesFor(EnumSet<TemplateField> fields) {
        Set<String> stringTemplates = new LinkedHashSet<>();
        registry.forEach((key, value) -> {
            if (fields.containsAll(key)) {
                stringTemplates.addAll(value.templates());
            }
        });

        // TODO:  Move elsewhere?
        if(stringTemplates.size() == 0) {
            //stringTemplates.addAll(DEFAULT_KEYWORD_TEMPLATES.templates());
            //LOGGER.debug("Using default keyword templates");
            LOGGER.warn("NO TEMPLATES LOADED");
        }
        return stringTemplates;
    }

    public static TemplateRegistry defaults() {
        Map<EnumSet<TemplateField>, TemplateSet> registry = new HashMap<>();
        TemplateSet defaultTemplateSet = TemplateSet.of(
                "${KEYWORD}.${NUM10}",
                "${KEYWORD}${NUM10}",
                "${KEYWORD}${NUM10}0",
                "${KEYWORD}${NUM10}00"
        );
        registry.put(EnumSet.of(TemplateField.KEYWORD1, TemplateField.NUM10), defaultTemplateSet);
        return new TemplateRegistry(registry);
    }

    public static Map<EnumSet<TemplateField>, TemplateSet> defaultTemplateMap() {
        return defaults().getTemplateMap();
    }
}
