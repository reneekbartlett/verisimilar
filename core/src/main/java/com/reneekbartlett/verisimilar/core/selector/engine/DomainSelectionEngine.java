package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.DomainDatasetResult;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class DomainSelectionEngine extends AbstractSelectionEngine<DomainDatasetKey,DomainDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    @SuppressWarnings("unused")
    private final Map<DomainType, Double> domainTypesMap;
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey(DomainType domainType) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$domain");
            if(domainType != null) sb.append("$"+domainType);
            return sb.toString();
        }
    }

    public DomainSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public DomainSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
        this.domainTypesMap = DomainType.defaultMap();
    }

    protected void setup() {
        DomainDatasetResult domainDatasetResult = resolvers.domain().resolve(DomainDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(domainDatasetResult.datasets().size());
        domainDatasetResult.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });
    }

    @Override
    public String select(DomainDatasetKey key, SelectionFilter filter) {
        DomainType domainType = filter.domainType().orElse(DomainType.B2C);

        NameKey nameKey = new NameKey(domainType);
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            // rebuild without domainDomain
            SelectionFilter domainFilter = filter.toBuilder().build();
            selector.setFilter(domainFilter);
        }

        return selector.select();
    }

    @Override
    public DomainDatasetKey defaultKey() {
        return DomainDatasetKey.defaults();
    }

    @Override
    public Class<DomainDatasetKey> keyType() {
        return DomainDatasetKey.class;
    }

    @Override
    public Class<DomainDatasetResult> resultType() {
        return DomainDatasetResult.class;
    }
}
