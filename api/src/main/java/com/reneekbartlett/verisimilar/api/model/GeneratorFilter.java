package com.reneekbartlett.verisimilar.api.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.FilterOperator;
//import org.apache.commons.lang3.math.NumberUtils;
//import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
//import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public record GeneratorFilter(
        Map<String, FilterCondition> filters
) {
    public GeneratorFilter() {
        this(HashMap.newHashMap(0));
    }

    public SelectionFilter getSelectionFilter() {
        return getSelectionFilterBuilder().build();
    }

    /**
     * 
     * @return
     */
    public SelectionFilter.Builder getSelectionFilterBuilder() {
        // TODO:  Include List<TemplateField> filterFields?
        SelectionFilter.Builder builder = SelectionFilter.builder();
        for (Map.Entry<String, FilterCondition> entry : filters.entrySet()) {
            //String fieldLabel = entry.getKey();
            FilterCondition filterCondition = entry.getValue();
            String value = filterCondition.filterValue();

            switch (filterCondition.operator()) {
                case EQUAL_TO:
                    handleEq(builder, filterCondition.field(), value);
                    break;
                case IN:
                    handleIn(builder, filterCondition.field(), value);
                    break;
                case STARTS_WITH:
                    builder.addFilter(value, filterCondition.field(), FilterOperator.STARTS_WITH);
                    break;
                case CONTAINS:
                    builder.addFilter(value, filterCondition.field(), FilterOperator.CONTAINS);
                    break;
                case ENDS_WITH:
                    builder.addFilter(value, filterCondition.field(), FilterOperator.ENDS_WITH);
                    break;
                default:
                    builder.addFilter(value, filterCondition.field(), filterCondition.operator());
                    break;
                    // ignore or log
            }
        }

        return builder;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0);
        sb.append("GeneratorFilter=[");
        //sb.append("apiKey=" + apiKey + ",");
        //sb.append("filter count=" + filters != null ? filters.size() : "NULL");
        sb.append("]");
        return sb.toString();
    }

    // -----------------------------
    // Operator Handlers
    // -----------------------------

    // TODO: 8/21
    private void handleEq(SelectionFilter.Builder builder, TemplateField field, String filterValue) {
        Class<?> targetType = field.targetType();

        // Enum
        if (EnumSet.class.equals(targetType) || Enum.class.isAssignableFrom(targetType)) {
            Class<? extends Enum<?>> enumType = field.enumType();
            Enum<?> enumValue = getEnumValue(targetType, enumType, filterValue);
            //EnumSet<?> enumValues = convertToEnumSet(enumType, toFilter);
            builder.addFilter(Set.of(enumValue), field, FilterOperator.EQUAL_TO);
            return;
        }

        // Integer
        if (Integer.class.equals(targetType)) {
            builder.addFilter(filterValue, field, FilterOperator.EQUAL_TO);
            return;
        }

        // String
        if (String.class.equals(targetType)) {
            builder.addFilter(filterValue, field, FilterOperator.EQUAL_TO);
            return;
        }

        if (Date.class.equals(targetType) || LocalDate.class.equals(targetType)) {
            builder.addFilter(filterValue, field, FilterOperator.EQUAL_TO);
            return;
        }

        //throw new UnsupportedOperationException("Operator not supported: " + operator);
        throw new IllegalArgumentException("Unsupported EQ target type: " + targetType);
    }

    private void handleIn(SelectionFilter.Builder builder, TemplateField field, String filterValue) {
        Class<?> targetType = field.targetType();
        Set<String> toFilter = Arrays.stream(filterValue.split(","))
                .map(String::trim).collect(Collectors.toSet());

        if(toFilter.size() == 0) {
            return;
        }

        // EnumSet<T>
        if (EnumSet.class.equals(targetType) || Enum.class.isAssignableFrom(targetType)) {
            // Convert String to EnumSet
            EnumSet<?> enumValues = convertToEnumSet(field.enumType(), toFilter);
            builder.addFilter(enumValues, field, FilterOperator.IN);
            return;
        }

        // String list
        if (String.class.equals(targetType)) {
            builder.addFilter(toFilter, field, FilterOperator.IN);
            return;
        }

        // Integer list
        if (Integer.class.equals(targetType)) {
            // TODO:  Keep as String?
            //Set<Integer> integerSet = toFilter.stream()
            //        .filter(NumberUtils::isCreatable) // Only allow string values that are valid numbers
            //        .map(NumberUtils::toInt) // Converts safely to a primitive int
            //        .collect(Collectors.toSet());
            //addFilter(builder, field, FilterOperator.IN, toFilter);
            builder.addFilter(toFilter, field, FilterOperator.IN);
            return;
        }

        throw new IllegalArgumentException("Unsupported IN target type: " + targetType);
    }

    private static Enum<?> getEnumValue(Class<?> clazz, Class<? extends Enum<?>> enumType, String filterValue) {
        // Stream through the constants to find a name match
        Enum<?> enumValue = Arrays.stream(enumType.getEnumConstants())
            .filter(e -> e.name().equals(filterValue))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown enum constant " + enumType.getName() + "." + filterValue
            ));
        return enumValue;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> EnumSet<E> convertToEnumSet(Class<? extends Enum<?>> clazz, Set<String> strings) {
        // Cast class to exact recursive bound EnumSet, Initialize empty EnumSet using the class reference
        // then populate the set
        Class<E> enumClass = (Class<E>) clazz;
        EnumSet<E> result = EnumSet.noneOf(enumClass);
        for (String value : strings) {
            result.add(Enum.valueOf(enumClass, value));
        }
        return result;
    }
}
