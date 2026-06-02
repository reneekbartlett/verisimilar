package com.reneekbartlett.verisimilar.core.selector.engine;

import java.util.EnumSet;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.WeightedEnumData;
import com.reneekbartlett.verisimilar.core.selector.UniformEnumSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.WeightedEnumSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class EnumSelectionEngine<E extends Enum<E> & WeightedEnumData> {

//    public record NameKey(String searchStr) {}
//    public record EnumDatasetResult(Map<String, Double> dataset) implements DatasetResult {
//        @Override public Map<String, Double> getDefault() { return dataset; }
//    }
//    public record EnumDatasetKey(String id) implements DatasetKey {
//        public EnumDatasetKey() { this("ENUMKEY"); }
//    }

    private final TemplateField field;
    private final WeightedEnumSelectorImpl<E> weightedSelector;
    private final UniformEnumSelectorImpl<E> uniformSelector;

    private EnumSelectionEngine(EnumSet<E> enumSet, TemplateField field) {
        this.field = field;
        this.weightedSelector = new WeightedEnumSelectorImpl<>(enumSet, field);
        this.uniformSelector = new UniformEnumSelectorImpl<>(enumSet, field);
    }

    public String select(SelectionFilter filter) {
        if (weightedSelector == null && uniformSelector == null) {
            throw new IllegalStateException("No selector registered for " + this.field.getLabel());
        }

        if(filter != null && !filter.isEmpty()) {
            if(filter.equalToMap().containsKey(field)) {
                return filter.equalToMap().get(field);
            }
            weightedSelector.setFilter(filter);
        }

        return weightedSelector.select().getLabel();
    }

}
