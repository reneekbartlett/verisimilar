package com.reneekbartlett.verisimilar.core.selector.engine;

import com.reneekbartlett.verisimilar.core.datasets.resolver.DatasetResolver;
import com.reneekbartlett.verisimilar.core.model.TemplateField;

interface SelectionEngine<K,R> {

    abstract K defaultKey();
    abstract Class<K> keyType();
    abstract Class<R> resultType();
    abstract TemplateField field();
    abstract DatasetResolver<K, R> datasetResolver();

    //Map<String, Double> applyFilter(Map<String, Double> map, SelectionFilter filter);
    //List<String> applyFilter(List<String> values, SelectionFilter filter);
}
