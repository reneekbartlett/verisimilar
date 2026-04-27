package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.CityStateZipDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.CityStateZipDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class CityStateZipSelectionEngine extends AbstractSelectionEngine<CityStateZipDatasetKey,CityStateZipDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new UniformSelectorStrategy<>();
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey() {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$citystatezip");
            return sb.toString();
        }
    }

    public CityStateZipSelectionEngine(DatasetResolverRegistry resolvers) {
        super(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public CityStateZipSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    @Override
    protected void setup() {
        CityStateZipDatasetResult result = resolvers.cityStateZip().resolve(CityStateZipDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(result.datasets().size());
        result.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });
    }

    @Override
    public String select(CityStateZipDatasetKey key, SelectionFilter filter) {
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }
        if(filter != null && !filter.isEmpty()) {
            if(filter.equalToMap().containsKey(TemplateField.CITY)) {
                // TODO
                
            }
            selector.setFilter(filter);
        }
        return selector.select();
    }

    @Override
    public CityStateZipDatasetKey defaultKey() {
        return CityStateZipDatasetKey.defaults();
    }

    @Override
    public Class<CityStateZipDatasetKey> keyType() {
        return CityStateZipDatasetKey.class;
    }

    @Override
    public Class<CityStateZipDatasetResult> resultType() {
        return CityStateZipDatasetResult.class;
    }

    public TemplateField field() {
        return TemplateField.CITY;
    }
}
