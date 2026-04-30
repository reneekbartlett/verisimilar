package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.HashMap;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.KeywordDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.KeywordDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.KeywordDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.UniformSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class KeywordSelectionEngine extends AbstractSelectionEngine<KeywordDatasetKey, KeywordDatasetResult> {
    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new UniformSelectorStrategy<>();
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    public record NameKey() {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(0).append("dataset$keyword");
            return sb.toString();
        }
    }

    public KeywordSelectionEngine(DatasetResolverRegistry resolvers) {
        this(resolvers, DEFAULT_SELECTOR_STRATEGY);
    }

    public KeywordSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        super(resolvers, strategy);
    }

    public KeywordSelectionEngine(KeywordDatasetResolver resolver) {
        super(resolver, DEFAULT_SELECTOR_STRATEGY);
    }

    protected void setup() {
        KeywordDatasetResult keywordDatasetResult = resolvers.keyword().resolve(KeywordDatasetKey.defaults());
        this.selectorsByNameKey = HashMap.newHashMap(keywordDatasetResult.datasets().size());
        keywordDatasetResult.datasets().forEach((nameKey, map) -> {
            RandomSelector<String> selector = strategy.buildSelector(map);
            this.selectorsByNameKey.put(nameKey, selector);
        });
        LOGGER.debug("setup - KeywordDatasetResult:{}", keywordDatasetResult.toString());
    }

    @Override
    public String select(KeywordDatasetKey key, SelectionFilter filter) {
        // There are currently no NameKey parameters, so just get default.
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }

        if(filter != null && !filter.isEmpty()) {
            if(filter.equalToMap().containsKey(TemplateField.KEYWORD1)) {
                return filter.equalToMap().get(TemplateField.KEYWORD1);
            }
            selector.setFilter(filter);
        }

        return selector.select();
    }

    @Override
    public KeywordDatasetKey defaultKey() {
        return KeywordDatasetKey.defaults();
    }

    @Override
    public Class<KeywordDatasetKey> keyType() {
        return KeywordDatasetKey.class;
    }

    @Override
    public Class<KeywordDatasetResult> resultType() {
        return KeywordDatasetResult.class;
    }

    @Override
    protected TemplateField field() {
        return TemplateField.KEYWORD1;
    }

    public static SelectorStrategy<String> defaultSelectorStrategy(){
        return DEFAULT_SELECTOR_STRATEGY;
    }
}