package com.reneekbartlett.verisimilar.core.selector.engine;

import com.reneekbartlett.verisimilar.core.datasets.resolver.DatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.DatasetResult;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSelectionEngine<K,R> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSelectionEngine.class);
    protected final DatasetResolverRegistry resolvers;
    protected final SelectorStrategy<String> strategy;

    protected DatasetResolver<K, R> datasetResolver;

    protected AbstractSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        this.resolvers = resolvers;
        this.datasetResolver = resolvers.getResolver(this.keyType());
        this.strategy = strategy;
        setup();
    }

    protected AbstractSelectionEngine(DatasetResolver<K, R> datasetResolver, SelectorStrategy<String> strategy) {
        this.datasetResolver = datasetResolver;
        this.resolvers = null;
        this.strategy = strategy;
        setup();
    }

    /** Main entry point: resolve → filter → select. */
    public String select(K key, SelectionFilter filter) {
        DatasetResult dsResult = (DatasetResult) datasetResolver.resolve(key);
        RandomSelector<String> randomSelector = strategy.buildSelector(dsResult.getDefault());
        if (randomSelector == null) {
            throw new IllegalStateException("No selector registered for default NameKey.");
        }
        if(filter != null && !filter.isEmpty()) {
            randomSelector.setFilter(filter);
        }
        return randomSelector.select();
    }

    public String select(SelectionFilter filter) {
        return select(defaultKey(), filter);
    }

    public String select() {
        return select(defaultKey(), SelectionFilter.empty());
    }

    protected abstract void setup();

    public abstract K defaultKey();

    public abstract Class<K> keyType();

    public abstract Class<R> resultType();

    /**
     * Applies SelectionFilter to the dataset map.
     */
    protected Map<String, Double> applyFilter(Map<String, Double> map, SelectionFilter filter) {
        if(filter.customPredicate().isEmpty()) {
            return map;
        }
        LOGGER.debug("applyFilter started; filter:{}", filter);
        return EntryFilter.apply(map, filter);
    }
}
