package com.reneekbartlett.verisimilar.core.selector.filter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

public final class EntryFilter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(EntryFilter.class);

    private EntryFilter() {}

    public static <T> Map<T, Double> apply(
            Map<T, Double> values,
            SelectionFilter filter,
            TemplateField field
    ) {
        Predicate<String> predicate = buildPredicate(filter, field);
        return values.entrySet().stream()
                .filter(e -> predicate.test((String) e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public static List<String> apply(
            List<String> values,
            SelectionFilter filter,
            TemplateField field
    ) {
        Predicate<String> predicate = buildPredicate(filter, field);
        return values.stream()
                .filter(e -> predicate.test(e))
                .collect(Collectors.toList());
    }

    private static Predicate<String> buildPredicate(SelectionFilter filter, TemplateField field) {
        Predicate<String> p = s -> true;

        if (filter.customPredicates().isPresent()) {
            Set<SelectionPredicate<String>> predicates = filter.customPredicates().get();
            for(SelectionPredicate<String> selectionPredicate : predicates) {
                p = p.and(s-> selectionPredicate.test(s));
                //LOGGER.debug("selectionPredicate {}", selectionPredicate);
            }
        }

        if(field != null) {
            if(filter.startsWithMap().containsKey(field)) {
                String searchStr = filter.startsWithMap().get(field).toUpperCase();
                p = p.and(s -> s.toUpperCase().startsWith(searchStr));
                //LOGGER.debug("field {} startsWith {}", field.getPlaceholder(), searchStr);
            }
    
            if(filter.endsWithMap().containsKey(field)) {
                String searchStr = filter.endsWithMap().get(field).toUpperCase();
                p = p.and(s -> s.toUpperCase().endsWith(searchStr));
                //LOGGER.debug("field {} endsWith {}", field.getPlaceholder(), searchStr);
            }
    
            if(filter.containsMap().containsKey(field)) {
                String searchStr = filter.containsMap().get(field).toUpperCase();
                p = p.and(s -> s.toUpperCase().contains(searchStr));
                //LOGGER.debug("field {} contains {}", field.getPlaceholder(), searchStr);
            }
        }

        return p;
    }
}
