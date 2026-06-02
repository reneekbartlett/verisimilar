package com.reneekbartlett.verisimilar.core.selector;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.WeightedEnumData;

public class UniformEnumSelectorStrategy<E extends Enum<E> & WeightedEnumData> implements SelectorStrategy<E> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(UniformEnumSelectorStrategy.class);

    @Override
    public RandomSelector<E> buildSelector(Map<E, Double> weights, TemplateField field){
        EnumSet<E> enumSet = EnumSet.copyOf(weights.keySet());
        return new UniformEnumSelectorImpl<>(enumSet, field);
    }

    @Override
    public RandomSelector<E> buildSelector(Set<E> values, TemplateField field) {
        EnumSet<E> enumSet = EnumSet.copyOf(values);
        return new UniformEnumSelectorImpl<>(enumSet, field);
    }

    @Override
    public E select(Map<E, Double> map, TemplateField field) {
        EnumSet<E> enumSet = EnumSet.copyOf(map.keySet());
        RandomSelector<E> selector = new UniformEnumSelectorImpl<>(enumSet, field);
        return selector.select();
    }

    @Override
    public E select(E[] values, TemplateField field) {
        EnumSet<E> enumSet = EnumSet.copyOf(Arrays.asList(values));
        RandomSelector<E> selector = new UniformEnumSelectorImpl<>(enumSet, field);
        return selector.select();
    }

    @Override
    public String getType() {
        // TODO:  Make Enum
        return "UNIFORM";
    }
}
