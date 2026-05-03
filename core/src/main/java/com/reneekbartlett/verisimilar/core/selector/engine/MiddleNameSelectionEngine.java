package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.MiddleNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.MiddleNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.MiddleNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class MiddleNameSelectionEngine extends AbstractSelectionEngine<MiddleNameDatasetKey, MiddleNameDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<GenderIdentity, Double> genderIdentityMap;
    private RandomSelector<GenderIdentity> genderSelector;
    private Map<Ethnicity, Double> ethnicitiesMap;
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey(GenderIdentity gender, Ethnicity ethnicity) {
        public NameKey(GenderIdentity gender) {
            this(gender, Ethnicity.UNKNOWN);
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$middlename");
            if(gender != null) sb.append("$gender:"+gender.getPlaceholder());
            if(ethnicity != null) sb.append("$ethnicity:"+ethnicity.getPlaceholder());
            return sb.toString();
        }
    }

    public MiddleNameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public MiddleNameSelectionEngine(
            DatasetResolverRegistry resolvers, 
            SelectorStrategy<String> strategy
    ) {
        super(resolvers, strategy);
    }

    public MiddleNameSelectionEngine(MiddleNameDatasetResolver resolver) {
        super(resolver, DEFAULT_SELECTOR_STRATEGY);
    }

    protected void setup() {
        this.genderIdentityMap = GenderIdentity.defaultMap();
        this.genderSelector = new WeightedSelectorImpl<>(genderIdentityMap, TemplateField.GENDER_IDENTITY);

        // TODO:  Create cfg_fullname_middle_female_ETHNICITY.csv & cfg_fullname_middle_male_ETHNICITY.csv files.
        this.ethnicitiesMap = Map.of(Ethnicity.UNKNOWN, 0.0001);

        // This DatasetResult contains both genders, so only call once.
        MiddleNameDatasetResult middleNameDatasetResult = datasetResolver().resolve(MiddleNameDatasetKey.defaults());

        this.selectorsByNameKey = HashMap.newHashMap(middleNameDatasetResult.datasets().size());

        // Extract specific datasets (Map<String,Double>) and build selectors
        if(ethnicitiesMap.size() >= 1){
            for(Ethnicity ethnicity : ethnicitiesMap.keySet()){
                genderIdentityMap.keySet().forEach(gender -> {
                    NameKey nameKey = new NameKey(gender, ethnicity);
                    RandomSelector<String> selector = strategy.buildSelector(middleNameDatasetResult.get(nameKey), field());
                    selectorsByNameKey.put(nameKey, selector);
                });
            }
        }
        //LOGGER.debug("MiddleNameSelectioEngine setup complete");
        return;
    }

    @Override
    public String select(MiddleNameDatasetKey key, SelectionFilter filter) {
        if(filter != null && !filter.middleName().isEmpty()) {
            return filter.middleName().get();
        }

        // If GenderIdentity is not specified, pick a random 1 then only return first letter.
        GenderIdentity gender = filter.gender().orElse(genderSelector.select());

        // TODO: If Ethnicity is NOT supplied here, just select from dataset based on GenderIdentity.
        Ethnicity ethnicity = filter.ethnicity().orElse(Ethnicity.UNKNOWN);
        if(ethnicity != null && !ethnicitiesMap.containsKey(ethnicity)) {
            ethnicity = Ethnicity.UNKNOWN;
        }

        NameKey nameKey = new NameKey(gender, ethnicity);
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);

        if(selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            if(!filter.middleName().isEmpty()) {
                return filter.middleName().get();
            }

            if(filter.gender().isEmpty()) {
                // Just return first letter
                return selector.select().substring(0, 1);
            }
            selector.setFilter(filter);
        }

        LOGGER.debug("select middleName - nameKey={}; strategyType={};", nameKey, strategy.getType());
        return selector.select();
    }

    @Override
    protected Map<String, Double> applyFilter(
            Map<String, Double> map,
            SelectionFilter filter
    ) {
        return super.applyFilter(map, filter);
    }

    @Override
    public MiddleNameDatasetKey defaultKey() {
        return MiddleNameDatasetKey.defaults();
    }

    @Override
    public Class<MiddleNameDatasetKey> keyType() {
        return MiddleNameDatasetKey.class;
    }

    @Override
    public Class<MiddleNameDatasetResult> resultType() {
        return MiddleNameDatasetResult.class;
    }

    @Override
    protected TemplateField field() {
        return TemplateField.MIDDLE_NAME;
    }
}
