package com.reneekbartlett.verisimilar.core.templates.loader;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.TemplateSet;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 
 */
public class TemplateRegistryLoader {

    private final Yaml yaml = new Yaml();

    public TemplateRegistry loadFromClasspath(String resourcePath) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Template YAML not found: " + resourcePath);
            }

            Map<String, List<String>> raw = yaml.load(in);
            Map<Set<TemplateField>, TemplateSet> registry = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : raw.entrySet()) {
                Set<TemplateField> fields = parseFieldSet(entry.getKey());
                TemplateSet set = new TemplateSet(new LinkedHashSet<>(entry.getValue()));
                registry.put(fields, set);
            }
            return new TemplateRegistry(registry);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load template registry from YAML: " + resourcePath, e);
        }
    }

    private Set<TemplateField> parseFieldSet(String key) {
        Set<TemplateField> fields = new LinkedHashSet<>();
        for (String part : key.split(",")) {
            fields.add(TemplateField.valueOf(part.trim()));
        }
        return fields;
    }

    /*public static TemplateRegistry loadFromInternal() {
        Map<Set<TemplateField>, TemplateSet> registry = new HashMap<>();
        registry.put(Set.of(TemplateField.FIRST_NAME), TemplateSet.of("${FIRST}123", "${FIRST}999"));
        registry.put(Set.of(TemplateField.FIRST_NAME, TemplateField.LAST_NAME), TemplateSet.of("${FIRST}${LAST}"));
        registry.put(Set.of(TemplateField.FIRST_NAME, TemplateField.LAST_NAME, TemplateField.BIRTHDAY),
                TemplateSet.of("${FIRST}${LAST}${BIRTHDAY_YEAR}"));
        return new TemplateRegistry(registry);
    }*/

}
