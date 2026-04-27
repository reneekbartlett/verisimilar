package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import com.reneekbartlett.verisimilar.core.datasets.key.AddressTwoDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.AddressTwoDatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * Map<String, Double> ADDRESS2_UNIT_TYPES_WEIGHTED = ResourceMapLoader.loadDoubleMap("/address_unit_types_weighted.txt");
 * APARTMENT,1.0
 * 
 * 
 */
public class AddressTwoSelectionEngine extends AbstractSelectionEngine<AddressTwoDatasetKey, AddressTwoDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey() {
        @Override
        public String toString() {
            return new StringBuilder(0).append("dataset$addresstwo").toString();
        }
    }

    public AddressTwoSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public AddressTwoSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    protected void setup() {
        AddressTwoDatasetResult result = resolvers.address2().resolve(AddressTwoDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(1);
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });
    }

    @Override
    public String select(AddressTwoDatasetKey key, SelectionFilter filter) {
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            selector.setFilter(filter);
        }
        return selector.select();
    }

    @Override
    public AddressTwoDatasetKey defaultKey() {
        return AddressTwoDatasetKey.defaults();
    }

    @Override
    public Class<AddressTwoDatasetKey> keyType() {
        return AddressTwoDatasetKey.class;
    }

    @Override
    public Class<AddressTwoDatasetResult> resultType() {
        return AddressTwoDatasetResult.class;
    }
}
