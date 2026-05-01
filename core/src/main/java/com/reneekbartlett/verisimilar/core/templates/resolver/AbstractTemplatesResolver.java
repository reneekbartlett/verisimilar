package com.reneekbartlett.verisimilar.core.templates.resolver;

import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;

public abstract class AbstractTemplatesResolver<K, R> implements TemplatesResolver<K, R> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplatesResolver.class);

    //private final DatasetCache<K, R> templateCache = new DatasetCache<>();

    protected final TemplateRegistryLoader loader;
    protected final String defaultTemplatesFile;
    protected TemplateRegistry templateRegistry;

    protected AbstractTemplatesResolver(TemplateRegistryLoader loader, String defaultTemplatesFile) {
        this.loader = loader;
        this.defaultTemplatesFile = defaultTemplatesFile;
        this.templateRegistry = loader.loadFromClasspath(defaultTemplatesFile);
    }

    @Override
    public Set<String> getTemplates(EnumSet<TemplateField> populatedFields) {
        Set<String> filteredTemplates;
        if(templateRegistry == null) {
            // TODO: Check logic
            filteredTemplates = TemplateRegistry.defaults().getTemplatesFor(EnumSet.of(TemplateField.KEYWORD1));
        } else {
            filteredTemplates = templateRegistry.getTemplatesFor(populatedFields);
        }
        return filteredTemplates;
    }

    @Override
    public abstract Class<K> keyType();
}
