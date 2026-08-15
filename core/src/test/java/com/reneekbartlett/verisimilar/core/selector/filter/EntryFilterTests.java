package com.reneekbartlett.verisimilar.core.selector.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.TemplateField;

public class EntryFilterTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryFilterTests.class);

    // com.reneekbartlett.verisimilar.core.selector.engine
    // AbstractSelectionEngine
    @Test
    public void applyFilterTest() {
        //TemplateField field = TemplateField.ETHNICITY;
        //Map<Ethnicity, Double> valueMap = Ethnicity.defaultMap();
        //SelectionFilter filter = SelectionFilter.builder().ethnicities(EnumSet.of(Ethnicity.ASIAN)).build();
        //LOGGER.debug("applyFilter started; filter:{}", filter);
        //Map<Ethnicity, Double> result = testApply(valueMap, filter, field);

        Map<String, Double> firstNameValueMap = Map.of(
                "RENEE", 1.000,
                "CHIP", 1.000,
                "JOE", 1.000,
                "ROGER", 1.000,
                "RAY", 1.000
        );
        SelectionFilter firstNameFilter = SelectionFilter.builder()
                .addFilter("R", TemplateField.FIRST_NAME, "startswith")
                .build();
        Map<String, Double> filteredFirstNames = testApply(firstNameValueMap, firstNameFilter, TemplateField.FIRST_NAME);
 
        // assertThat all keys start with R
        assertThat(filteredFirstNames).containsOnlyKeys("RENEE", "ROGER", "RAY");

        Map<String, Double> lastNameValueMap = Map.of(
                "BARTLETT", 1.000,
                "DOGG", 1.000,
                "SMITH", 1.000,
                "BATES", 1.000,
                "Schmidt", 1.000
        );
        SelectionFilter lastNameFilter = SelectionFilter.builder()
                .addFilter("T", TemplateField.LAST_NAME, "endswith")
                .build();

        Map<String, Double> filteredLastNames = testApply(lastNameValueMap, lastNameFilter, TemplateField.LAST_NAME);

        // assertThat all keys end with T
        assertThat(filteredLastNames).containsOnlyKeys("BARTLETT", "Schmidt");

        LOGGER.debug("done");
    }
    
    @Test
    public void applyCityStateZipFilterTest() {
        Map<String, Double> cityStateZipValueMap = Map.of(
                "SHREWSBURY$MA$01545", 1.000,
                "SHREWSBURY$CT$01000", 1.000
        );
        SelectionFilter cityStateZipFilter = SelectionFilter.builder()
                .addFilter("SH", TemplateField.CITY, "startswith")
                .build();
        Map<String, Double> filteredCityStateZips = testApply(cityStateZipValueMap, cityStateZipFilter, TemplateField.CITY_STATE_ZIP);
 
        // assertThat all keys start with SH
        assertThat(filteredCityStateZips).containsOnlyKeys("SHREWSBURY$MA$01545", "SHREWSBURY$CT$01000");
    }

    private static <T> Map<T, Double> testApply(
            Map<T, Double> values,
            SelectionFilter filter,
            TemplateField field
    ) {
        SelectionPredicate<String> entryFilterPredicate = EntryFilter.buildPredicate(filter, field);
        LOGGER.debug("apply Map<T, Double> values -> entryFilterPredicate {}", entryFilterPredicate.asString());

        //Predicate<String> testPredicate = testBuildPredicate(filter, field);
        //LOGGER.debug("apply Map<T, Double> values -> testPredicate {}", testPredicate);

        return values.entrySet().stream()
                .filter(e -> entryFilterPredicate.test((String) e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    private static SelectionPredicate<String> testBuildPredicate(SelectionFilter filter, TemplateField field){

        Set<SelectionPredicate<String>> predicateSet = filter.customPredicates().get();

        Map<TemplateField, String> startsWithMap = filter.startsWithMap();
        Map<TemplateField, String> endsWithMap = filter.endsWithMap();
        Map<TemplateField, String> containsMap = filter.containsMap();

        Map<TemplateField, String> equalToMap = filter.equalToMap();
        Map<TemplateField, Set<String>> inMap = filter.inMap();
        Map<TemplateField, Set<?>> inEnumMap = filter.inEnumMap();

        SelectionPredicate<String> entryFilterPredicate = EntryFilter.buildPredicate(filter, field);

        SelectionPredicate<String> testPredicate = s -> true;

        // Chain together set of predicates
        if (!predicateSet.isEmpty()) {
            for(SelectionPredicate<String> selectionPredicate : predicateSet) {
                testPredicate = testPredicate.and(s-> selectionPredicate.test(s));
            }
            LOGGER.debug("testPredicate {}", testPredicate);
        }

        if(field != null) {
            if(filter.startsWithMap().containsKey(field)) {
                String searchStr = startsWithMap.get(field).toUpperCase();
                testPredicate = testPredicate.and(s -> s.toUpperCase().startsWith(searchStr));
                LOGGER.debug("field {} startsWith {}", field.getPlaceholder(), searchStr);
            }
    
            if(filter.endsWithMap().containsKey(field)) {
                String searchStr = endsWithMap.get(field).toUpperCase();
                testPredicate = testPredicate.and(s -> s.toUpperCase().endsWith(searchStr));
                LOGGER.debug("field {} endsWith {}", field.getPlaceholder(), searchStr);
            }
    
            if(filter.containsMap().containsKey(field)) {
                String searchStr = containsMap.get(field).toUpperCase();
                testPredicate = testPredicate.and(s -> s.toUpperCase().contains(searchStr));
                LOGGER.debug("field {} contains {}", field.getPlaceholder(), searchStr);
            }
        }

        LOGGER.debug("entryFilterPredicate={}", entryFilterPredicate.asString());
        LOGGER.debug("testPredicate={}", testPredicate.asString());

        return testPredicate;
    }

}
