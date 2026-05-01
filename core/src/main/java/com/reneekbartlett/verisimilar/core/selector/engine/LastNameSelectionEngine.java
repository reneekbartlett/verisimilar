package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.LastNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.datasets.resolver.LastNameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class LastNameSelectionEngine extends AbstractSelectionEngine<LastNameDatasetKey, LastNameDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<Ethnicity, Double> ethnicitiesMap;
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey(Ethnicity ethnicity) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$lastname");
            if(ethnicity != null) sb.append("$ethnicity:" + ethnicity.getPlaceholder());
            return sb.toString();
        }
    }

    public LastNameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public LastNameSelectionEngine(
            DatasetResolverRegistry resolvers, 
            SelectorStrategy<String> strategy
    ) {
        super(resolvers, strategy);
    }

    public LastNameSelectionEngine(LastNameDatasetResolver resolver) {
        super(resolver, DEFAULT_SELECTOR_STRATEGY);
    }

    protected void setup() {
        this.ethnicitiesMap = Ethnicity.defaultMap();
        //this.ethnicitiesMap = Map.of(Ethnicity.UNKNOWN, 0.0001);

        // Extract specific datasets (Map<String,Double>), build selectors, and add to selectorsByNameKey
        LastNameDatasetResult lastNameDatasetResult = datasetResolver().resolve(LastNameDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(5);
        if(!ethnicitiesMap.isEmpty() && ethnicitiesMap.keySet().size() >= 1) {
            // TODO:  revise?
            EnumSet<Ethnicity> customEthnicities = EnumSet.copyOf(ethnicitiesMap.keySet());
            for(Ethnicity e : customEthnicities){
                Map<String,Double> map = lastNameDatasetResult.get(new NameKey(e));
                if(!map.isEmpty()) {
                    RandomSelector<String> s1 = strategy.buildSelector(map);
                    selectorsByNameKey.put(new NameKey(e), s1);
                }
            }
        }
    }

    @Override
    public String select(LastNameDatasetKey key, SelectionFilter filter) {
        // If Ethnicity is NOT supplied here, just select from generic dataset.
        Ethnicity ethnicity = filter.ethnicity().orElse(Ethnicity.UNKNOWN);
        if(!ethnicitiesMap.containsKey(ethnicity)) {
            ethnicity = Ethnicity.UNKNOWN;
        }

        NameKey nameKey = new NameKey(ethnicity);
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            selector.setFilter(filter);
        }

        //LOGGER.debug("select lastName - nameKey={}; strategyType={};", nameKey, strategy.getType());
        return selector.select();
    }

    @Override
    public LastNameDatasetKey defaultKey() {
        return LastNameDatasetKey.defaults();
    }

    @Override
    public Class<LastNameDatasetKey> keyType() {
        return LastNameDatasetKey.class;
    }

    @Override
    public Class<LastNameDatasetResult> resultType() {
        return LastNameDatasetResult.class;
    }

    @Override
    protected TemplateField field() {
        return TemplateField.LAST_NAME;
    }
}
