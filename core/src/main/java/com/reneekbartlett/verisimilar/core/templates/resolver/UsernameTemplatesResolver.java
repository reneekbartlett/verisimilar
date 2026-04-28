package com.reneekbartlett.verisimilar.core.templates.resolver;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.TemplateSet;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine.TemplateParameters;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;

public class UsernameTemplatesResolver extends AbstractTemplatesResolver<Set<TemplateField>, TemplateRegistry> {

    private static final String TEMPLATES_FILE = "templates/username-templates.yaml";

    public record UsernameTemplatesResult(
            Set<TemplateField> populatedfields,
            Map<Set<TemplateField>, TemplateSet> templateMap
    ) {
        // TODO:  change to Set<String> instead?
        public TemplateSet getTemplates() {
            Set<String> stringTemplates = new LinkedHashSet<>();
            templateMap.forEach((key, value) -> {
                if (populatedfields.containsAll(key)) {
                    stringTemplates.addAll(value.templates());
                }
            });
            return new TemplateSet(stringTemplates);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("UsernameTemplatesResult");
            if(populatedfields != null) sb.append("$populatedfields=" + populatedfields.size());
            if(templateMap != null) sb.append("$templates=" + templateMap.size());
            return sb.toString();
        }
    }

    public UsernameTemplatesResolver(TemplateRegistryLoader loader) {
        super(loader, TEMPLATES_FILE);
    }

    @Override
    public Class<TemplateRegistry> resultType() {
        return TemplateRegistry.class;
    }

    @Override
    public Class<Set<TemplateField>> keyType() {
        return null;
    }

    public UsernameTemplatesResult loadForFields(TemplateParameters templateParameters) {
        Set<TemplateField> populatedFields = templateParameters.populatedFields();
        return new UsernameTemplatesResult(populatedFields, super.templateRegistry.getTemplateMap());
    }

    //@Override
    public UsernameTemplatesResult loadForFields(Set<TemplateField> populatedFields) {
        
        // TODO:

        return new UsernameTemplatesResult(populatedFields, super.templateRegistry.getTemplateMap());
    }

    public Set<TemplateField> detectPopulatedFields(SelectionFilter filter) {
        Set<TemplateField> fields = new HashSet<>();
        if (filter.firstName().isPresent()) fields.add(TemplateField.FIRST_NAME);
        if (filter.lastName().isPresent()) fields.add(TemplateField.LAST_NAME);
        if (filter.middleName().isPresent()) fields.add(TemplateField.MIDDLE_NAME);
        if (filter.birthday().isPresent()) fields.add(TemplateField.BIRTHDAY);
        //if (key.gender() != null) fields.add(TemplateField.GENDER);
        LOGGER.debug("detectPopulatedFields - fields:" + fields.toString());
        return fields;
    }

}
