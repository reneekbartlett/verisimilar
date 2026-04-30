package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import com.reneekbartlett.verisimilar.core.datasets.key.AddressTwoDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.AddressTwoDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.datasets.resolver.AddressTwoDatasetResolver;
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

    //private static final String[] ADDRESS2_UNIT_XTRA = { "A", "B", "C", "D", "E", "F", "N", "S", "E", "W" };
    // TODO:  Templates address2-templates.yaml

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

    public AddressTwoSelectionEngine(AddressTwoDatasetResolver resolver) {
        super(resolver, DEFAULT_SELECTOR_STRATEGY);
    }

    protected void setup() {
        AddressTwoDatasetResult result = resolvers.address2().resolve(AddressTwoDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(result.datasets().size());
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });
    }

    @Override
    public String select(AddressTwoDatasetKey key, SelectionFilter filter) {
        // There are currently no NameKey parameters, so just get default.
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            if(filter.equalToMap().containsKey(TemplateField.ADDRESS2)) {
                return filter.equalToMap().get(TemplateField.ADDRESS2);
            }
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

    @Override
    protected TemplateField field() {
        return TemplateField.ADDRESS2;
    }
}
