package com.reneekbartlett.verisimilar.core.selector;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

/***
 * UniformSelectorImpl<T>
 * @param <T>
 */
public final class UniformSelectorImpl<T> implements RandomSelector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniformSelectorImpl.class);

    private final List<T> dataset;
    private final TemplateField field;
    private final int valueCount;

    private volatile SelectionFilter filter;

    // memoization cache
    private final ConcurrentMap<SelectionFilter, UniformSelectorImpl<T>> filteredCache = new ConcurrentHashMap<>();

    public UniformSelectorImpl(List<T> dataset, TemplateField field) {
        Objects.requireNonNull(dataset, "dataset");
        Objects.requireNonNull(field, "TemplateField");
        if (dataset.isEmpty()) {
            LOGGER.error("Dataset cannot be empty");
            throw new IllegalArgumentException("Dataset cannot be empty");
        }
        this.field = field;
        this.dataset = dataset;
        this.valueCount = this.dataset.size();
    }

    public UniformSelectorImpl(Set<T> dataset, TemplateField field) {
        Objects.requireNonNull(dataset, "dataset");
        if (dataset.isEmpty()) {
            LOGGER.error("Dataset cannot be empty");
            throw new IllegalArgumentException("Dataset cannot be empty");
        }
        this.field = field;
        this.dataset = List.copyOf(dataset);
        this.valueCount = this.dataset.size();
    }

    public UniformSelectorImpl(Map<T, Double> dataset, TemplateField field) {
        Objects.requireNonNull(dataset, "dataset");
        if (dataset.isEmpty()) {
            LOGGER.error("Weighted dataset map cannot be empty");
            throw new IllegalArgumentException("Weighted dataset map cannot be empty");
        }
        this.field = field;
        this.dataset = List.copyOf(dataset.keySet());
        this.valueCount = this.dataset.size();
    }

    /***
     * TODO:  Document filter process
     */
    @Override
    public T select() {
        SelectionFilter filter = this.filter;
        if(filter != null && !filter.isEmpty()) {
            UniformSelectorImpl<T> filtered = filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
            LOGGER.debug("select {} with filter", field.getLabel());
            return filtered.select();
        }
        // select un-filtered
        LOGGER.debug("select {} without filter", field.getLabel());
        int idx = ThreadLocalRandom.current().nextInt(valueCount);
        return dataset.get(idx);
    }

    @Override
    public int getValueCount(){
        return this.valueCount;
    }

    /***
     * 
     * @param filter
     * @return
     */
    // TODO: Check EntryFilter.applyToList
    private UniformSelectorImpl<T> buildFilteredSelector(SelectionFilter filter) {
        List<T> filtered = EntryFilter.applyToList(dataset, filter, field);
        if (filtered.isEmpty()) {
            LOGGER.warn("Filtered dataset list empty for filter: {}", filter);
            return new UniformSelectorImpl<T>(dataset, field); // fallback to original
            //return this; // fallback to original
        }
        LOGGER.debug("Filtered selector: original={}, filtered={}, filter={}",
                dataset.size(), filtered.size(), filter);

        if(field == TemplateField.CITY_STATE_ZIP || field == TemplateField.STATE 
                || field == TemplateField.CITY || field == TemplateField.ZIP_CODE) {
            LOGGER.debug("{}", field.getLabel());
        }

        return new UniformSelectorImpl<T>(filtered, field);
    }

    public void setFilter(SelectionFilter filter) {
        this.filter = filter;
        LOGGER.debug("Set {} filter={}", field.getLabel(), filter);
    }
}
