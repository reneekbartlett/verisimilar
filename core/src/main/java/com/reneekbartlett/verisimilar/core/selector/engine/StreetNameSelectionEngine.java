package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.StreetNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class StreetNameSelectionEngine extends AbstractSelectionEngine<StreetNameDatasetKey,StreetNameDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey() {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$streetname");
            return sb.toString();
        }
    }

    public StreetNameSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public StreetNameSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    protected void setup() {
        StreetNameDatasetResult streetNameDatasetResult = resolvers.streetName().resolve(StreetNameDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(1);
        streetNameDatasetResult.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });

        //RandomSelector<String> selector = strategy.buildSelector(Set.of(streetNameDatasetResult.all()));
        //selectorsByNameKey.put(new NameKey(), selector);
        LOGGER.debug("streetSuffixDatasetResult=[{}]", streetNameDatasetResult);
    }

    @Override
    public String select(StreetNameDatasetKey key, SelectionFilter filter) {
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && filter.startsWithMap().containsKey(TemplateField.STREET_NAME)) {
            SelectionFilter streetNameFilter = SelectionFilter.builder()
                    .startsWith(filter.startsWithMap().get(TemplateField.STREET_NAME)).build();
            selector.setFilter(streetNameFilter);
        }

        return selector.select();
    }

    @Override
    public StreetNameDatasetKey defaultKey() {
        return StreetNameDatasetKey.defaults();
    }

    @Override
    public Class<StreetNameDatasetKey> keyType() {
        return StreetNameDatasetKey.class;
    }

    @Override
    public Class<StreetNameDatasetResult> resultType() {
        return StreetNameDatasetResult.class;
    }

    public TemplateField field() {
        return TemplateField.STREET_NAME;
    }
}
