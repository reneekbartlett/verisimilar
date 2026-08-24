package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.datasets.key.DatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.key.FirstNameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.DatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.datasets.result.DatasetResult;
import com.reneekbartlett.verisimilar.core.datasets.result.FirstNameDatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFieldMapper;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter.Builder;

/***
 * DomainSelectionEngine extends AbstractSelectionEngine<DomainDatasetKey,DomainDatasetResult>
 * @param <K>   DatasetKey      DomainDatasetKey
 * @param <R>   DatasetResult   DomainDatasetResult
 * 
 * DatasetResolverRegistry resolvers
 * SelectorStrategy<String> strategy
 */
abstract class AbstractSelectionEngine<K,R> implements SelectionEngine<K,R> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSelectionEngine.class);
    protected final SelectorStrategy<String> strategy;
    protected final DatasetResolver<K, R> datasetResolver;

    protected AbstractSelectionEngine(DatasetResolver<K, R> datasetResolver, SelectorStrategy<String> strategy) {
        this.datasetResolver = datasetResolver;
        this.strategy = strategy;
        setup();
    }

    protected AbstractSelectionEngine(DatasetResolverRegistry resolvers, SelectorStrategy<String> strategy) {
        this.datasetResolver = resolvers.getResolver(this.keyType());
        this.strategy = strategy;
        setup();
    }

    /** Main entry point: resolve → filter → select. */
    public String select(K key, SelectionFilter filter) {
        if(filter != null && filter.equalToMap().containsKey(field())) {
            String filterValue = filter.equalToMap().get(field());
            LOGGER.debug("select {}, {}, {}", key, filter, field());
            return filterValue;
        }

        DatasetResult dsResult = (DatasetResult) datasetResolver.resolve(key);
        RandomSelector<String> randomSelector = strategy.buildSelector(dsResult.getDefault(), field());
        if (randomSelector == null) {
            LOGGER.warn("{}", dsResult.getDefault());
            throw new IllegalStateException("No selector registered for default NameKey. [Field=" + field().getLabel() + "]");
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

    @Override
    public abstract K defaultKey();

    @Override
    public abstract Class<K> keyType();

    @Override
    public abstract Class<R> resultType();

    @Override
    public abstract TemplateField field();

    @Override
    public DatasetResolver<K, R> datasetResolver(){
        return this.datasetResolver;
    }

    /**
     * Applies SelectionFilter to the dataset map.
     */
    protected Map<String, Double> applyFilter(Map<String, Double> map, SelectionFilter filter) {
        if(filter.isEmpty()) {
            return map;
        }
        LOGGER.debug("applyFilter started; filter:{}", filter);
        return EntryFilter.applyToMap(map, filter, field());
    }

    protected List<String> applyFilter(List<String> values, SelectionFilter filter) {
        if(filter.isEmpty()) {
            return values;
        }
        LOGGER.debug("applyFilter started; filter:{}", filter);
        return EntryFilter.applyToList(values, filter, field());
    }

    // TODO:  Not implemented.
    public enum SelectionEngineMapper {
        FIRST_NAME(
            TemplateField.FIRST_NAME,
            FirstNameSelectionEngine.class,
            //(datasetResolver, strategy) -> new FirstNameSelectionEngine(datasetResolver, strategy)
            null
        );
        private final TemplateField templateField;
        private final Class<?> generatorType;
        private final BiFunction<DatasetResolver<DatasetKey,DatasetResult>, 
            SelectorStrategy<String>, AbstractSelectionEngine<DatasetKey,DatasetResult>> singleConsumer;
        //private final BiFunction<Builder, Set<?>, Builder> multiConsumer;
    
        // Static lookup cache for fast, non-loop O(1) performance
        //static final Map<TemplateField, SelectionFieldMapper> LOOKUP = Arrays.stream(values())
        //        .collect(Collectors.toMap(SelectionFieldMapper::getTemplateField, Function.identity()));
    
        SelectionEngineMapper(
                TemplateField templateField, 
                Class<?> generatorType,
                BiFunction<DatasetResolver<DatasetKey,DatasetResult>, SelectorStrategy<String>, 
                AbstractSelectionEngine<DatasetKey,DatasetResult>> singleConsumer 
                //BiFunction<Builder, Set<?>, Builder> multiConsumer
        ) {
            //Map<String, Double> map
            //DatasetResolver<K, R> datasetResolver, SelectorStrategy<String> strategy
            
            //RandomSelector<String> randomSelector = strategy.buildSelector(dsResult.getDefault(), field());
            this.templateField = templateField;
            this.generatorType = generatorType;
            this.singleConsumer = singleConsumer;
            //this.multiConsumer  = multiConsumer;
        }
    }
}
