package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.EnumSet;
import java.util.Map;

import com.reneekbartlett.verisimilar.core.datasets.key.StreetSuffixDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.result.DatasetResult;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.SelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorStrategy;
import com.reneekbartlett.verisimilar.core.selector.engine.StreetSuffixSelectionEngine.NameKey;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class EnumSelectionEngine {

    private static final SelectorStrategy<String> DEFAULT_SELECTOR_STRATEGY = new WeightedSelectorStrategy<>();
    private Map<NameKey, RandomSelector<String>> selectorsByNameKey;

    private final EnumSet<?> enumSet;
    private final TemplateField field;

    private EnumSelectionEngine(EnumSet<?> enumSet, TemplateField field) {
        this.enumSet = enumSet;
        this.field = field;
    }

    public static EnumSelectionEngine create(EnumSet<?> enumSet, TemplateField field) {
        return new EnumSelectionEngine(enumSet, field);
    }

    public String select(StreetSuffixDatasetKey key, SelectionFilter filter) {
        //RandomSelector<String> randomSelector = strategy.buildSelector(dsResult.getDefault(), field());

        //DEFAULT_SELECTOR_STRATEGY.buildSelector(null, field)
        DatasetResult dsResult;

        // There are currently no NameKey parameters, so just get default.
        NameKey nameKey = new NameKey();
        RandomSelector<String> selector = selectorsByNameKey.get(nameKey);
        if (selector == null) {
            throw new IllegalStateException("No selector registered for " + nameKey);
        }
        if(filter != null && !filter.isEmpty()) {
            if(filter.equalToMap().containsKey(field)) {
                return filter.equalToMap().get(field);
            }

            if(filter.startsWithMap().containsKey(field)) {
                //TODO
            }

            selector.setFilter(filter);
        }
        return selector.select();
    }

}
