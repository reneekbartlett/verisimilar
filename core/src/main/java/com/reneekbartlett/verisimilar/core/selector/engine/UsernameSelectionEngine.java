package com.reneekbartlett.verisimilar.core.selector.engine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
import com.reneekbartlett.verisimilar.core.templates.TemplateRegistry;
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

    public UsernameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public UsernameSelectionEngine(
            DatasetResolverRegistry resolvers, 
            SelectorStrategy<String> strategy
    ) {
        super(resolvers, strategy);
    }

    public UsernameSelectionEngine(
            UsernameDatasetResolver usernameDatasetResolver, 
            SelectorStrategy<String> strategy,
            TemplateRegistry templateRegistry
    ) {
        super(usernameDatasetResolver, strategy);
    }

    protected void setup() {
        UsernameDatasetResult result = super.datasetResolver.resolve(UsernameDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(result.datasets().size());
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });
    }

    @Override
    public String select(UsernameDatasetKey key, SelectionFilter filter) {
        //
        // Keyword
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            //throw new IllegalStateException("No selector registered for " + nameKey);
            return "";
        }

        if(!filter.isEmpty()) {
            selector.setFilter(filter);
        }

        String usernameKeyword1 = selector.select().toUpperCase();
        String usernameKeyword2 = selector.select().toUpperCase();

        //
        // Templates
        UsernameTemplatesResolver templatesResolver = new UsernameTemplatesResolver(new TemplateRegistryLoader());

        TemplateParameters parameters = getTemplateParameters(filter, usernameKeyword1, usernameKeyword2);

        Map<String, Object> personTemplateParams = parameters.resolved();

        Map<String, Object> allTemplateParams = new HashMap<>(personTemplateParams);

        // TODO: Add 
        Set<TemplateField> populatedFields = parameters.populatedFields();

        UsernameTemplatesResult templatesResult = templatesResolver.loadForFields(populatedFields);
        LOGGER.debug("templatesResult:{}", templatesResult.toString());

        TemplateSet templateSet = templatesResult.getTemplates();
        // Add default? 

        if(templateSet.templates().size() == 0) {
            LOGGER.warn("No Templates...");
        }

        // Pick a template
        // TODO:  Backup?  "${KEYWORD}${NUM1000}"
        UniformSelectorImpl<String> templateSelector = new UniformSelectorImpl<>(templateSet.toList());
        String randomTemplate = templateSelector.select();

        String usernameFromTemplate = applyTemplate(randomTemplate, usernameKeyword1, allTemplateParams);
        LOGGER.debug("randomTemplate:{}; usernameFromTemplate:{}", randomTemplate, usernameFromTemplate);

        return usernameFromTemplate;
    }

    public record TemplateParameters(Set<UsernameTemplateParam> usernameTemplateFields) {
        public Set<TemplateField> populatedFields(){
            return usernameTemplateFields.stream().map(x -> x.templateField).collect(Collectors.toSet());
        }

        public Map<String, Object> resolved(){
            // TODO:  Use placeholder instead? TemplateField.BIRTHDAY.getPlaceholder()
            //usernameTemplateFields.stream().map(x -> x.fieldValue())
            Map<String, Object> resolvedValueParams = HashMap.newHashMap(usernameTemplateFields.size());
            for(UsernameTemplateParam templateParam : usernameTemplateFields) {
                templateParam.templateField.getPlaceholder();
                templateParam.fieldValue();
                resolvedValueParams.put(templateParam.templateField.getPlaceholder(), templateParam.fieldValue());
            }

            if(resolvedValueParams.containsKey("BIRTHDAY")) {
                //LocalDate birthday = (LocalDate) resolvedValueParams.get("BIRTHDAY");
                LocalDate birthday = LocalDate.parse((String)resolvedValueParams.get("BIRTHDAY"));
                AstrologySign sign = AstrologySign.fromLocalDate(birthday);
                resolvedValueParams.put("BIRTHDAY_YEAR", String.valueOf(birthday.getYear()));
                resolvedValueParams.put("BIRTHDAY_YEAR_SHORT", String.valueOf(birthday.getYear()).substring(2));
                resolvedValueParams.put("BIRTHDAY_DAY", String.valueOf(birthday.getDayOfMonth()));
                resolvedValueParams.put("BIRTHDAY_SIGN", sign.name());
            }

            if(resolvedValueParams.containsKey("FIRST")) {
                String firstName = (String) resolvedValueParams.get("FIRST");
                resolvedValueParams.put("FIRST_INITIAL", firstName.charAt(0));
            }

            if(resolvedValueParams.containsKey("LAST")) {
                 String lastName = (String) resolvedValueParams.get("LAST");
                 resolvedValueParams.put("LAST_INITIAL", lastName.charAt(0));
            }

            if(resolvedValueParams.containsKey("MIDDLE")) {
                 String middleName = (String) resolvedValueParams.get("MIDDLE");
                 resolvedValueParams.put("MIDDLE_INITIAL", middleName.charAt(0));
            }
            
            

            return resolvedValueParams;
        }

        public Map<String, Object> general(){
            Map<String, Object> generalValueParams = new HashMap<>();
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            generalValueParams.put(TemplateField.fromValue("NUM10").getPlaceholder(), String.valueOf(rand.nextInt(100)));
            generalValueParams.put(TemplateField.fromValue("NUM100").getPlaceholder(), String.valueOf(rand.nextInt(1000)));
            generalValueParams.put(TemplateField.fromValue("NUM1000").getPlaceholder(), String.valueOf(rand.nextInt(1000,9999)));
            generalValueParams.put("SEPARATOR", ".");
            return generalValueParams;
        }

        public Map<String, Object> keyword(String... keywords){
            Map<String, Object> keywordValueParams = new HashMap<>();
            int i = 1;
            for(String keyword : keywords) {
                TemplateField keywordField = TemplateField.fromValue("KEYWORD" + String.valueOf(i));
                if(keywordField != null) {
                    keywordValueParams.put(keywordField.getPlaceholder(), keyword);
                    i++;
                } else {
                    break;
                }
            }
            return keywordValueParams;
        }
    }

    public record UsernameTemplateParam(TemplateField templateField, String fieldValue) {}

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

//        ThreadLocalRandom rand = ThreadLocalRandom.current();
//        params.put("NUM10", String.valueOf(rand.nextInt(100)));
//        params.put("NUM100", String.valueOf(rand.nextInt(1000)));
//        params.put("NUM1000", String.valueOf(rand.nextInt(1000,9999)));
//    
//        //TemplateField.BIRTHDAY.getPlaceholder()
//        if(params.containsKey("BIRTHDAY")) {
//            LocalDate birthday = (LocalDate) params.get("BIRTHDAY");
//            params.put("BIRTHDAY_YEAR", String.valueOf(birthday.getYear()));
//            params.put("BIRTHDAY_YEAR_SHORT", String.valueOf(birthday.getYear()).substring(2));
//            params.put("BIRTHDAY_DAY", String.valueOf(birthday.getDayOfMonth()));
//    
//            AstrologySign sign = AstrologySign.fromLocalDate(birthday);
//            params.put("BIRTHDAY_SIGN", sign.name());
//        } 
//    
//        if(params.containsKey("FIRST")) {
//            String firstName = (String) params.get("FIRST");
//            params.put("FIRST_INITIAL", firstName.charAt(0));
//        }
//    
//        if(params.containsKey("LAST")) {
//            String lastName = (String) params.get("LAST");
//            params.put("LAST_INITIAL", lastName.charAt(0));
//        }
//    
//        if(params.containsKey("MIDDLE")) {
//            String middleName = (String) params.get("MIDDLE");
//            params.put("MIDDLE_INITIAL", middleName.charAt(0));
//        }

    }
}
