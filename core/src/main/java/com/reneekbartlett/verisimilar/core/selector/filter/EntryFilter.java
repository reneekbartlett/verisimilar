package com.reneekbartlett.verisimilar.core.selector.filter;

import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

/***
 * EntryFilter
 */
public final class EntryFilter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(EntryFilter.class);

    private EntryFilter() {}

    public static <T> Map<T, Double> apply(
            Map<T, Double> values,
            SelectionFilter filter,
            TemplateField field
    ) {
        SelectionPredicate<String> predicate = buildPredicate(filter, field);
        LOGGER.debug("apply Map<T, Double> values -> predicate {}", predicate);
        return values.entrySet().stream()
                .filter(e -> predicate.test((String) e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    /***
     * 
     */
    private static Set<String> apply(
            Set<String> values,
            SelectionFilter filter,
            TemplateField field
    ) {
        SelectionPredicate<String> predicate = buildPredicate(filter, field);
        LOGGER.debug("apply Set<String> values -> predicate {}", predicate);
        return values.stream()
                .filter(e -> predicate.test(e))
                .collect(Collectors.toSet());
    }

    /***
     * 
     * @param <T>
     * @param values
     * @param filter
     * @param field
     * @return
     */
    public static <T> List<T> applyToList(
            List<T> values,
            SelectionFilter filter,
            TemplateField field
    ) {
        // Map T to String while preserving order
        SelectionPredicate<String> predicate = buildPredicate(filter, field);
        LOGGER.debug("applyToList -> predicate {}", predicate);
        return values.stream()
                .filter(e -> predicate.test((String) e))
                .collect(Collectors.toList());
    }

    /***
     * 
     * @param filter
     * @param field
     * @return
     */
    protected static SelectionPredicate<String> buildPredicate(SelectionFilter filter, TemplateField field) {
        SelectionPredicate<String> p = s -> true;

        // Set
        if (filter.customPredicates().isPresent()) {
            for(SelectionPredicate<String> selectionPredicate : filter.customPredicates().get()) {
                p = p.and(s-> selectionPredicate.test(s));
                LOGGER.debug("selectionPredicate AND {}", selectionPredicate.asString());
            }
        }

        if(field != null) {
            if(filter.startsWithMap().containsKey(field)) {
                String searchStr = filter.startsWithMap().get(field).toUpperCase();
                p = p.and(s -> s.toUpperCase().startsWith(searchStr));
                LOGGER.debug("field {} startsWith {}", field.getPlaceholder(), searchStr);
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
