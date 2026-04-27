package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.UsernameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.UsernameType;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UsernameTemplateSelector;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;

/***
 * USERNAME_TEMPLATES_NAME
 * USERNAME_TEMPLATES_POPULAR
 * List<String> USERNAME_KEYWORDS    ResourceValueLoader.loadStringList("/username_keywords.txt")
 * Map<String, String[]> NICKNAME_MAP
 */
public class UsernameSelectionEngine extends AbstractSelectionEngine<UsernameDatasetKey, UsernameDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private final TemplateRegistry templateRegistry;

    protected Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey(UsernameType usernameType) {
        public NameKey() {
            this(UsernameType.KEYWORD1); // default
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$username");
            if(usernameType != null) sb.append("$"+usernameType);
            return sb.toString();
        }
    }

    public UsernameSelectionEngine(DatasetResolverRegistry resolvers, TemplateRegistry templateRegistry) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY, templateRegistry);
    }

    public UsernameSelectionEngine(
            DatasetResolverRegistry resolvers, 
            SelectorStrategy<String> strategy,
            TemplateRegistry templateRegistry
    ) {
        super(resolvers, strategy);
        this.templateRegistry = templateRegistry;
    }

    public UsernameSelectionEngine(
            UsernameDatasetResolver usernameDatasetResolver, 
            SelectorStrategy<String> strategy,
            TemplateRegistry templateRegistry
    ) {
        super(usernameDatasetResolver, strategy);
        this.templateRegistry = templateRegistry;
    }

    protected void setup() {
        UsernameDatasetResult result = super.datasetResolver.resolve(UsernameDatasetKey.defaults());
        //UsernameDatasetResult result = resolvers.username().resolve(UsernameDatasetKey.defaults());

        this.selectorsByNameKey = HashMap.newHashMap(1);

        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });

        // Username Keywords
        RandomSelector<String> selector = strategy.buildSelector(result.keywords());
        selectorsByNameKey.put(new NameKey(), selector);
        LOGGER.debug("setup - result:{}", result.toString());
    }

    /***
     * Override AbstractSelectionEngine
     */
    @Override
    public String select(UsernameDatasetKey key, SelectionFilter filter) {
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }
        if(!filter.isEmpty()) {
            selector.setFilter(filter);
        }
        return selector.select().toUpperCase();
    }

    // TODO:  I don't like this..
    public UsernameTemplateSelector getTemplateSelector(UsernameDatasetKey key, SelectionFilter filter) {
        Set<TemplateField> populatedFields = new HashSet<>();
        populatedFields.add(TemplateField.KEYWORD); // always add TemplateField.KEYWORD
        populatedFields.addAll(detectPopulatedFields(filter));

        Set<String> filteredTemplates;
        if(templateRegistry == null) {
            filteredTemplates = TemplateRegistry.defaults()
                    .getTemplatesFor(Set.of(TemplateField.KEYWORD));
        } else {
            filteredTemplates = templateRegistry.getTemplatesFor(populatedFields);
        }
        //return new UsernameTemplateSelector(result.keywords(), filteredTemplates, populatedFields);
        return new UsernameTemplateSelector(filteredTemplates, populatedFields);
    }

    private Set<TemplateField> detectPopulatedFields(SelectionFilter key) {
        Set<TemplateField> fields = new HashSet<>();
        if (key.firstName().isPresent()) fields.add(TemplateField.FIRST_NAME);
        if (key.lastName().isPresent()) fields.add(TemplateField.LAST_NAME);
        if (key.middleName().isPresent()) fields.add(TemplateField.MIDDLE_NAME);
        //if (key.gender() != null) fields.add(TemplateField.GENDER);
        if (key.birthday().isPresent()) fields.add(TemplateField.BIRTHDAY);
        LOGGER.debug("detectPopulatedFields - fields:" + fields.toString());
        return fields;
    }

    @Override
    public UsernameDatasetKey defaultKey() {
        return UsernameDatasetKey.defaults();
    }

    public static SelectorStrategy<String> defaultSelectorStrategy(){
        return new UniformSelectorStrategy<>();
    }

    @Override
    public Class<UsernameDatasetKey> keyType() {
        return UsernameDatasetKey.class;
    }

    @Override
    public Class<UsernameDatasetResult> resultType() {
        return UsernameDatasetResult.class;
    }
}
