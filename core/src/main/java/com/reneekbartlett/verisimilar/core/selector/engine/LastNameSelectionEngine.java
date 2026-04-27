package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.reneekbartlett.verisimilar.core.datasets.key.LastNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.LastNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
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
            if(ethnicity != null) sb.append("$" + ethnicity.name());
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

    protected void setup() {
        // TODO:  Check logic here.  Maybe just iterate result.dataSets()
        this.ethnicitiesMap = Ethnicity.defaultMap();

        LastNameDatasetResult lastNameDatasetResult = resolvers.last().resolve(LastNameDatasetKey.defaults());

        this.selectorsByNameKey = HashMap.newHashMap(5);

        Set<Ethnicity> customEthnicities = Ethnicity.defaultMap().keySet();
        if(customEthnicities.size() >= 1){
            for(Ethnicity e : customEthnicities){
                //LastNameDatasetResult ds1 = resolvers.last().resolve(new LastNameDatasetKey(e));
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

        LOGGER.debug("select lastName - nameKey={}; strategyType={};", nameKey, strategy.getType());
        return selector.select();

        // FILTER BEFORE SELECTION
        //Map<String, Double> filtered = EntryFilter.apply(result.all(), filter);
        //WeightedSelector<String> selector = strategy.buildSelector(filtered);
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
}
