package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;
import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.DomainDatasetResult;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.datasets.resolver.DomainDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class DomainSelectionEngine extends AbstractSelectionEngine<DomainDatasetKey,DomainDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    @SuppressWarnings("unused")
    private Map<DomainType, Double> domainTypesMap;
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey(DomainType domainType) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$domain");
            if(domainType != null) sb.append("$domainType:"+domainType);
            return sb.toString();
        }
    }

    public DomainSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public DomainSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    public DomainSelectionEngine(DomainDatasetResolver resolver) {
        super(resolver, DEFAULT_SELECTOR_STRATEGY);
    }

    protected void setup() {
        this.domainTypesMap = DomainType.defaultMap();
        DomainDatasetResult domainDatasetResult = resolvers.domain().resolve(DomainDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(domainDatasetResult.datasets().size());
        domainDatasetResult.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            selectorsByNameKey.put(nameKey, selector);
        });
    }

    @Override
    public String select(DomainDatasetKey key, SelectionFilter filter) {
        // If domainType is already specified, use it.  Otherwise, get a random one.
        DomainType domainType = filter.domainType().orElseGet(this::getRandomDomainType);

        NameKey nameKey = new NameKey(domainType);
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            if(filter.equalToMap().containsKey(TemplateField.DOMAIN)) {
                return filter.equalToMap().get(TemplateField.DOMAIN);
            }

            selector.setFilter(filter);

            // rebuild without domainDomain
            //SelectionFilter domainFilter = filter.toBuilder().build();
            //selector.setFilter(domainFilter);
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

    @Override
    protected TemplateField field() {
        return TemplateField.DOMAIN;
    }

    private DomainType getRandomDomainType() {
        RandomSelector<DomainType> domainTypeSelector = new WeightedSelectorImpl<>(domainTypesMap);
        return domainTypeSelector.select();
    }
}
