package com.reneekbartlett.verisimilar.core.selector.engine;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.text.StringSubstitutor;

import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.UsernameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.AstrologySign;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.TemplateSet;
import com.reneekbartlett.verisimilar.core.model.UsernameType;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.templates.TemplateParameters;
import com.reneekbartlett.verisimilar.core.templates.UsernameTemplateParam;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;
import com.reneekbartlett.verisimilar.core.templates.resolver.UsernameTemplatesResolver;
import com.reneekbartlett.verisimilar.core.templates.resolver.UsernameTemplatesResolver.UsernameTemplatesResult;

/***
 * USERNAME_TEMPLATES_NAME
 * USERNAME_TEMPLATES_POPULAR
 * List<String> USERNAME_KEYWORDS    ResourceValueLoader.loadStringList("/username_keywords.txt")
 * Map<String, String[]> NICKNAME_MAP
 */
public class UsernameSelectionEngine extends AbstractSelectionEngine<UsernameDatasetKey, UsernameDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new UniformSelectorStrategy<>();

    protected HashMap<NameKey, RandomSelector<String>> selectorsByNameKey;
    //protected ConcurrentHashMap<NameKey, RandomSelector<String>> selectorsByNameKeyV2;

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

    public UsernameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public UsernameSelectionEngine(
            DatasetResolverRegistry resolvers, 
            SelectorStrategy<String> strategy
    ) {
        super(resolvers.username(), strategy);
    }

    public UsernameSelectionEngine(UsernameDatasetResolver usernameDatasetResolver) {
        super(usernameDatasetResolver, DEFAULT_SELECTOR_STRATEGY);
    }

    protected void setup() {
        UsernameDatasetResult result = datasetResolver().resolve(UsernameDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(result.datasets().size());
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map, field());
            selectorsByNameKey.put(nameKey, selector);
        });
        LOGGER.debug("setup - {}", selectorsByNameKey.keySet());
        /*this.selectorsByNameKey = result.datasets().entrySet().stream().collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> strategy.buildSelector(entry.getValue(), field()),
                    (existing, replacement) -> existing, // Merge function (if needed)
                    () -> HashMap.newHashMap(result.datasets().size()) // Preserves pre-sizing
        ));*/
    }

    @Override
    public String select(UsernameDatasetKey key, SelectionFilter filter) {
        //
        // Keyword
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(!filter.isEmpty()) {
            String valueFilter = filter.equalToMap().get(field());
            if (valueFilter != null) {
                return valueFilter;
            }
            selector.setFilter(filter);
        }

        String usernameKeyword1 = selector.select().toUpperCase();
        String usernameKeyword2 = selector.select().toUpperCase();
        
        LOGGER.debug("usernameKeyword1={}", usernameKeyword1);
        LOGGER.debug("usernameKeyword2={}", usernameKeyword2);

        //
        // Templates
        UsernameTemplatesResolver templatesResolver = new UsernameTemplatesResolver(new TemplateRegistryLoader());
        TemplateParameters parameters = getTemplateParameters(filter, usernameKeyword1, usernameKeyword2);
        Map<String, Object> allTemplateParams = new HashMap<>(parameters.resolved());
        UsernameTemplatesResult templatesResult = templatesResolver.loadForFields(parameters.populatedFields());

        TemplateSet templateSet = templatesResult.getTemplates();
        if(templateSet.templates().size() == 0) {
            LOGGER.warn("No Templates...");
        }

        // Pick a template
        // TODO:  Backup?  "${KEYWORD}${NUM1000}"
        UniformSelectorImpl<String> templateSelector = new UniformSelectorImpl<>(templateSet.toList(), TemplateField.TEMPLATE);
        String randomTemplate = templateSelector.select();
        String usernameFromTemplate = applyTemplate(randomTemplate, usernameKeyword1, allTemplateParams);
        //LOGGER.debug("randomTemplate:{}; usernameFromTemplate:{}", randomTemplate, usernameFromTemplate);
        //LOGGER.trace("templatesResult:{}", templatesResult.toString());

        return usernameFromTemplate;
    }

    

    private TemplateParameters getTemplateParameters(SelectionFilter filter, String... keywords) {
        Set<UsernameTemplateParam> templateParams = new HashSet<>();
        if (filter.firstName().isPresent())
            templateParams.add(new UsernameTemplateParam(TemplateField.FIRST_NAME, filter.firstName().get()));
        if (filter.lastName().isPresent()) 
            templateParams.add(new UsernameTemplateParam(TemplateField.LAST_NAME, filter.lastName().get()));
        if (filter.middleName().isPresent()) 
            templateParams.add(new UsernameTemplateParam(TemplateField.MIDDLE_NAME, filter.middleName().get()));
        if (filter.birthday().isPresent()) 
            templateParams.add(new UsernameTemplateParam(TemplateField.BIRTHDAY, filter.birthday().get().toString()));
        //if (key.gender() != null) 
        //  templateParams.add(new UsernameTemplateParam(TemplateField.GENDER, filter.gender().get().toString()));

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        templateParams.add(new UsernameTemplateParam(TemplateField.NUM10, String.valueOf(rand.nextInt(100))));
        templateParams.add(new UsernameTemplateParam(TemplateField.NUM100, String.valueOf(rand.nextInt(1000))));
        templateParams.add(new UsernameTemplateParam(TemplateField.NUM1000, String.valueOf(rand.nextInt(1000,9999))));

        templateParams.add(new UsernameTemplateParam(TemplateField.SEPARATOR, "."));

        int i = 1;
        for(String keyword : keywords) {
            TemplateField keywordField = TemplateField.fromValue("KEYWORD" + String.valueOf(i));
            if(keywordField != null) {
                templateParams.add(new UsernameTemplateParam(keywordField, keyword));
                i++;
            } else {
                // TODO:  fix this..
                templateParams.add(new UsernameTemplateParam(TemplateField.KEYWORD1, keyword));
                break;
            }
        }

        LOGGER.debug("getTemplateParameters:" + templateParams.size());

        return new TemplateParameters(templateParams);
    }

    @Override
    public UsernameDatasetKey defaultKey() {
        return UsernameDatasetKey.defaults();
    }

    @Override
    public Class<UsernameDatasetKey> keyType() {
        return UsernameDatasetKey.class;
    }

    @Override
    public Class<UsernameDatasetResult> resultType() {
        return UsernameDatasetResult.class;
    }

    @Override
    public TemplateField field() {
        return TemplateField.USERNAME;
    }

    public static SelectorStrategy<String> defaultSelectorStrategy(){
        return DEFAULT_SELECTOR_STRATEGY;
    }

    /***
     * 
     * @param template
     * @param randomKeyword
     * @param resolvedValues  Map<String, Object>  Key: Placeholder; Value: String Replacement
     * @return
     */
    private String applyTemplate(String template, String randomKeyword, 
            Map<String, Object> resolvedValues
    ) {
        // TODO:  Use TemplateField
        // Then Add Username Templates and Apply Values
        Map<String, Object> params = new HashMap<>(resolvedValues);

        if(!params.containsKey("KEYWORD")) {
            params.put("KEYWORD", randomKeyword);
        }

        if(!params.containsKey("SEPARATOR")) {
            params.put("SEPARATOR", ".");
        }

        return StringSubstitutor.replace(template, params);
    }
}
