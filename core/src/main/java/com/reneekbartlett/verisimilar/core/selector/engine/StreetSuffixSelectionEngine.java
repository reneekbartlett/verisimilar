package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetSuffixDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetSuffixDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class StreetSuffixSelectionEngine extends AbstractSelectionEngine<StreetSuffixDatasetKey,StreetSuffixDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey() {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$streetsuffix");
            return sb.toString();
        }
    }

    public StreetSuffixSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public StreetSuffixSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    protected void setup() {
        StreetSuffixDatasetResult streetSuffixDatasetResult = resolvers.streetSuffix().resolve(StreetSuffixDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(1);
        final Map<String, Double> map = new HashMap<>();
        streetSuffixDatasetResult.suffixAbbr().forEach((k,v) -> {
            map.put(k, 1.000);
            map.put(v, 1.000);
        });
        RandomSelector<String> selector = strategy.buildSelector(map);
        selectorsByNameKey.put(new NameKey(), selector);

        //streetSuffixDatasetResult.datasets().forEach((nameKey, map) -> {
        //    RandomSelector<String> selector = strategy.buildSelector(map);
         //   selectorsByNameKey.put(nameKey, selector);
        //});

        LOGGER.debug("streetSuffixDatasetResult=[{}]", streetSuffixDatasetResult);
    }

    @Override
    public String select(StreetSuffixDatasetKey key, SelectionFilter filter) {
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }
        if(filter != null && filter.startsWithMap().containsKey(field())) {
            selector.setFilter(filter);
        }
        return selector.select();
    }

    @Override
    public StreetSuffixDatasetKey defaultKey() {
        return StreetSuffixDatasetKey.defaults();
    }

    @Override
    public Class<StreetSuffixDatasetKey> keyType() {
        return StreetSuffixDatasetKey.class;
    }

    @Override
    public Class<StreetSuffixDatasetResult> resultType() {
        return StreetSuffixDatasetResult.class;
    }

    public TemplateField field() {
        return TemplateField.STREET_SUFFIX;
    }
}
