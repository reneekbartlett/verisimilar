package com.reneekbartlett.verisimilar.core.selector;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.WeightedEnumData;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public final class UniformEnumSelectorImpl<E extends Enum<E> & WeightedEnumData> implements RandomSelector<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniformEnumSelectorImpl.class);

    private final EnumSet<E> enumSet;
    private final TemplateField field;
    private final int valueCount;
    private final List<E> dataset;

    private volatile SelectionFilter filter;

    // memoization cache
    private final ConcurrentMap<SelectionFilter, UniformEnumSelectorImpl<E>> filteredCache = new ConcurrentHashMap<>();

    public UniformEnumSelectorImpl(EnumSet<E> enumSet, TemplateField field) {
        Objects.requireNonNull(enumSet, "enumSet");
        if (enumSet.isEmpty()) {
            LOGGER.error("Dataset cannot be empty");
            throw new IllegalArgumentException("Dataset cannot be empty");
        }
        this.field = field;
        this.enumSet = enumSet;
        this.dataset = List.copyOf(enumSet); // copy to ensure immutability
        this.valueCount = this.dataset.size();
    }

    @Override
    public E select() {
        SelectionFilter filter = this.filter;
        if(filter != null && !filter.isEmpty()) {
            UniformEnumSelectorImpl<E> filtered = filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
            return filtered.select();
        }
        // select un-filtered
        int idx = ThreadLocalRandom.current().nextInt(valueCount);
        return dataset.get(idx);
    }

    @Override
    public int getValueCount(){
        return this.valueCount;
    }

    public UniformEnumSelectorImpl<E> withFilter(SelectionFilter filter) {
        if (filter == null || filter.isEmpty()) {
            return this;
        }
        return filteredCache.computeIfAbsent(filter, this::buildFilteredSelector);
    }

    private UniformEnumSelectorImpl<E> buildFilteredSelector(SelectionFilter filter) {
        List<E> filtered = EntryFilter.applyToList(dataset, filter, field);
        if (filtered.isEmpty()) {
            LOGGER.trace("Filtered dataset list empty for filter: {}", filter);
            return new UniformEnumSelectorImpl<E>(enumSet, field); // fallback to original
        }
        LOGGER.trace("Filtered selector: original={}, filtered={}, filter={}",
                dataset.size(), filtered.size(), filter);
        EnumSet<E> filteredEnumSet = EnumSet.copyOf(filtered);
        return new UniformEnumSelectorImpl<E>(filteredEnumSet, field);
    }

    public void setFilter(SelectionFilter filter) {
        this.filter = filter;
    }
}
