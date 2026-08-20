package com.reneekbartlett.verisimilar.core.selector.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

/***
 * EntryFilter
 */
public final class EntryFilter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(EntryFilter.class);

    private EntryFilter() {}

    public static <T> Map<T, Double> applyToMap(
            Map<T, Double> values,
            SelectionFilter filter,
            TemplateField field
    ) {
        //List<SelectionPredicate<String>> predicateList = new ArrayList<>();
        //Map<T, Double> filtered;

        if(field.equals(TemplateField.CITY_STATE_ZIP) 
                || field.equals(TemplateField.CITY)
                || field.equals(TemplateField.STATE)
                || field.equals(TemplateField.ZIP_CODE)) {
            SelectionPredicate<String> cityStateZipPredicate = getCityStateZipPredicate(filter, field);
            LOGGER.debug("EntryFilter.applyToMap -> CITY_STATE_ZIP / {}", field.getLabel());
            return values.entrySet().stream()
                    .filter(e -> cityStateZipPredicate.test((String) e.getKey()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                    ));
        }

        SelectionPredicate<String> predicate = buildPredicate(filter, field);

        LOGGER.debug("EntryFilter.applyToMap Map<T, Double> values -> {} predicate", field.getLabel());
        return values.entrySet().stream()
                .filter(e -> predicate.test((String) e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
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

        if(field.equals(TemplateField.CITY_STATE_ZIP) 
                || field.equals(TemplateField.CITY)
                || field.equals(TemplateField.STATE)
                || field.equals(TemplateField.ZIP_CODE)) {
            SelectionPredicate<String> cityStateZipPredicate = getCityStateZipPredicate(filter, field);
            LOGGER.debug("EntryFilter.applyToList -> CITY_STATE_ZIP cityStateZipPredicate {}", "");
            return values.stream()
                    .filter(e -> cityStateZipPredicate.test(String.valueOf(e)))
                    .collect(Collectors.toList());
        }

        SelectionPredicate<String> filterPredicate = buildPredicate(filter, field);

        // Map T to String while preserving order
        //SelectionPredicate<String> predicate = buildPredicate(filter, field);
        LOGGER.debug("EntryFilter.applyToList -> {}, filterPredicate {}", field.getLabel(), "");

        return values.stream()
                .filter(e -> filterPredicate.test(String.valueOf(e)))
                .collect(Collectors.toList());

    }

    public static SelectionPredicate<String> getCityStateZipPredicate(
            SelectionFilter filter, TemplateField field
    ) {
        final Set<String> citySet = new HashSet<>();
        final Set<String> stateSet = new HashSet<>();
        final Set<String> zipCodeSet = new HashSet<>();

        filter.city().ifPresent((city) -> citySet.add(city));

        filter.state().ifPresent((state) -> stateSet.add(state.getLabel()));
        filter.states().ifPresent((states) -> { 
            states.forEach(state -> stateSet.add(state.getLabel()));
        });

        filter.zipCode().ifPresent((zipCode) -> zipCodeSet.add(zipCode));
        filter.zipCodes().ifPresent((zipCodes) -> {
            zipCodeSet.addAll(zipCodes);
        });

        Set<String> patterns = new HashSet<>();
        Set<String> andPatterns = new HashSet<>();

        if(!citySet.isEmpty()) {
            for (String city : citySet) {
                if (stateSet.isEmpty()) {
                    // For CITY only (No state or zip)
                    patterns.add(city + "$");

                    // For CITY + ZIP (No state values), use andPatterns set.
                    for (String zip : zipCodeSet) {
                        andPatterns.add("$" + zip);
                    }
                } else {
                    for (String state : stateSet) {
                        if (zipCodeSet.isEmpty()) {
                            // FOR CITY + STATE (No zipCode values)
                            patterns.add(city + "$" + state + "$");
                        } else {
                            // FOR CITY + STATE + ZIP (Match full value)
                            for (String zip : zipCodeSet) {
                                patterns.add(city + "$" + state + "$" + zip);
                            }
                        }
                    }
                }
            }
        } else {
            // No City Value(s)
            if(!stateSet.isEmpty()) {
                for (String state : stateSet) {
                    if (zipCodeSet.isEmpty()) {
                        // STATE Only (No City or Zip)
                        patterns.add("$" + state + "$");
                    } else {
                        // STATE + ZIP (no city)
                        for (String zip : zipCodeSet) {
                            patterns.add("$" + state + "$" + zip);
                        }
                    }
                }
            } else {
                // ZIP only (No CITY value(s) or STATE value(s))
                for (String zip : zipCodeSet) {
                    patterns.add("$" + zip);
                }
            }
        }

        LOGGER.debug("patterns={}", patterns);
        LOGGER.debug("andPatterns={}", andPatterns);

        //
        // Filter for eq/in operators (eq, in) and passed value parameters.
        //
        SelectionPredicate<String> cityStateZipPredicate;
        if(patterns.size() >= 1) {
            cityStateZipPredicate = input -> {
                if (input == null) return false;
                String normalized = input.toUpperCase();
                for (String pattern : patterns) {
                    if(andPatterns.size() >= 1) {
                        for (String andPattern : andPatterns) {
                            if (normalized.contains(pattern) && normalized.contains(andPattern)) {
                                return true;
                            }
                        }
                    } else {
                        if (normalized.contains(pattern)) {
                            return true;
                        }
                    }
                }
                return false;
            };
        } else {
            cityStateZipPredicate = s -> true;
        }

        //
        // Filter values, like startsWith, endsWith, and contains
        //
        SelectionPredicate<String> filterPredicate;
        if(filter.customPredicateMap().containsKey(field)) {
            filterPredicate = s -> true;
            Set<DescribedPredicate<String>> customPredicates = filter.customPredicateMap().get(field);
            for(DescribedPredicate<String> customP : customPredicates) {
                LOGGER.debug("customP={}", customP.text());
                filterPredicate = filterPredicate.and(customP.condition());
            }
            LOGGER.debug("field {} found in customPredicateMap", field.getPlaceholder());
        } else {
            filterPredicate = null;
        }

        if(filterPredicate != null) {
            cityStateZipPredicate = cityStateZipPredicate.and(filterPredicate);
        }

        return cityStateZipPredicate;
    }

    /***
     * 
     * @param filter
     * @param field
     * @return
     */
    protected static SelectionPredicate<String> buildPredicate(SelectionFilter filter, TemplateField field) {
        SelectionPredicate<String> p = s -> true;
        //SelectionPredicate<String> p = s -> s != null && !s.strip().isEmpty();

        StringBuilder sb = new StringBuilder(0);

        if(field != null) {
            String fieldLabel = field.getLabel();

            if(filter.customPredicateMap().containsKey(field)) {
                Set<DescribedPredicate<String>> customPredicates = filter.customPredicateMap().get(field);
                for(DescribedPredicate<String> customP : customPredicates) {
                    p = p.and(customP.condition());
                    LOGGER.debug("customP={}", customP.text());
                }
                LOGGER.debug("field {} found in customPredicateMap {}", field.getPlaceholder());
            }

            if(filter.startsWithMap().containsKey(field)) {
                String searchStr = filter.startsWithMap().get(field).toUpperCase();
                p = p.and(s -> s.toUpperCase().startsWith(searchStr));
                sb.append(fieldLabel + " startsWith " + searchStr).append(",");
                LOGGER.debug("field {} startsWith {}", fieldLabel, searchStr);
            }
    
            if(filter.endsWithMap().containsKey(field)) {
                String searchStr = filter.endsWithMap().get(field).toUpperCase();
                p = p.and(s -> s.toUpperCase().endsWith(searchStr));
                sb.append(fieldLabel + " endswith " + searchStr).append(",");
                LOGGER.debug("field {} endsWith {}", fieldLabel, searchStr);
            }
    
            if(filter.containsMap().containsKey(field)) {
                String searchStr = filter.containsMap().get(field).toUpperCase();
                p = p.and(s -> s.toUpperCase().contains(searchStr));
                sb.append(fieldLabel + " contains " + searchStr).append(",");
                LOGGER.debug("field {} contains {}", fieldLabel, searchStr);
            }
        }

        var describedPredicate = new DescribedPredicate<String>(p, sb.toString());
        LOGGER.debug("describedPredicate for field {} [text='{}']", field.getLabel(), describedPredicate.text());

        // TODO: Check custom predicates for non-city_state_zip fields?

        return p;
    }

    public static <T> SelectionPredicate<String> equalTo(T value) {
        if (value == null) return str -> false;
        //Predicate<String> isAppleIgnoreCase = str -> str != null && str.equalsIgnoreCase(target);
        return SelectionPredicate.isEqual(value);
    }

    public static SelectionPredicate<String> containsSubstring(String substring, boolean ignoreCase) {
        if (substring == null) {
            return str -> false;
        }

        if (ignoreCase) {
            String lowerTarget = substring.toLowerCase(Locale.ROOT);
            return str -> str != null && str.toLowerCase(Locale.ROOT).contains(lowerTarget);
        }

        return str -> str != null && str.contains(substring);
    }

    public static SelectionPredicate<String> startsWithSubstring(String substring, boolean ignoreCase) {
        if (substring == null) return str -> false;
        if (ignoreCase) {
            String lowerTarget = substring.toLowerCase(Locale.ROOT);
            return str -> str != null && str.toLowerCase(Locale.ROOT).startsWith(lowerTarget);
        }
        return str -> str != null && str.startsWith(substring);
    }

    public static <T> SelectionPredicate<String> containsAnyFastest(List<T> list, String description) {
        if (list == null || list.isEmpty()) return str -> false;

        Trie trie = Trie.builder()
                .ignoreCase()
                .addKeywords(list.stream().map(Object::toString).toList())
                .stopOnHit() // Forces a fast short-circuit evaluation
                .build();

        return str -> str != null && !trie.parseText(str).isEmpty();
    }

    public static <T> SelectionPredicate<String> containsAllFastest(List<T> list) {
        if (list == null || list.isEmpty()) return str -> false;

        Set<String> keywords = list.stream()
                .filter(java.util.Objects::nonNull)
                .map(item -> item.toString().toLowerCase(java.util.Locale.ROOT))
                .collect(Collectors.toSet());

        Trie trie = Trie.builder()
                .ignoreCase()
                .addKeywords(keywords)
                .build();

        int targetUniqueCount = keywords.size();

        return str -> {
            if (str == null) {
                return false;
            }

            // 3. Parse text and extract unique matched keywords
            Collection<Emit> emits = trie.parseText(str);

            Set<String> matchedKeywords = emits.stream()
                    .map(Emit::getKeyword)
                    .collect(Collectors.toSet());

            // Verify if the count of unique matches equals our starting requirement
            return matchedKeywords.size() == targetUniqueCount;
        };
    }

    public record DescribedPredicate<T>(SelectionPredicate<T> condition, String text) {
        
        public static <T> SelectionPredicate<String> containsAnyOf(List<T> list) {
            return str -> list.stream()
                              .map(Object::toString)
                              .anyMatch(str::contains);
        }

        /***
         * For long list structures, loop within the predicate performs faster
         * @param keywords
         * @return
         */
        public static DescribedPredicate<String> containsAnyFast(List<String> keywords, String description){
            SelectionPredicate<String> containsAnyFast = str -> {
                for (String word : keywords) {
                    if (str.contains(word)) {
                        return true;
                    }
                }
                return false;
            };
            return new DescribedPredicate<String>(containsAnyFast, description);
        }

        // Generic method accepting any type T
        public static <T> DescribedPredicate<String> containsAnyOf(List<T> list, String description) {
            SelectionPredicate<String> containsAnyOf = str -> {
                if (str == null || list == null) return false;
                String lowerStr = str.toLowerCase(Locale.ROOT);
                for (T item : list) {
                    if (item != null) {
                        String itemStr = item.toString().toLowerCase(Locale.ROOT);
                        if (lowerStr.contains(itemStr)) {
                            return true; // Fast short-circuit
                        }
                    }
                }
                return false;
            };
            return new DescribedPredicate<String>(containsAnyOf, description);
        }

        /***
         * Pre-compiled Regular Expression (O(1) setup
         * @param keywords
         * @return
         */
        public static DescribedPredicate<String> containsAnyRegex(Collection<String> keywords, String description) {
            // Escape keywords and join them: (keyword1|keyword2|keyword3)
            String patternString = keywords.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|", "(", ")"));

            // CASE_INSENSITIVE flag replaces manual lowercase conversions
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

            SelectionPredicate<String> containsAnyRegex = str -> str != null && pattern.matcher(str).find();

            return new DescribedPredicate<String>(containsAnyRegex, description);
        }
    }

}
