package com.reneekbartlett.verisimilar.core.selector;

import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public interface RandomSelector<T> {
    T select();
    int getValueCount();
    void setFilter(SelectionFilter filter);
}
