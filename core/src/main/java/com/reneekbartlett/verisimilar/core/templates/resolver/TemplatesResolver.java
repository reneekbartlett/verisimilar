package com.reneekbartlett.verisimilar.core.templates.resolver;

import java.util.EnumSet;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

public interface TemplatesResolver<K, R> {

    /**
     * The key type this resolver supports.
     */
    Class<K> keyType();

    Class<R> resultType();

    /**
     * Resolve the template for the given key.
     */
    //R resolve(K key);

    Set<String> getTemplates(EnumSet<TemplateField> populatedFields);
}
