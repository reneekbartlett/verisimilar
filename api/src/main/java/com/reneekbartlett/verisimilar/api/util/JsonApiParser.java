package com.reneekbartlett.verisimilar.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.api.model.FilterOperator;

public class JsonApiParser {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonApiParser.class);

    private static final Pattern BRACKET_PATTERN = Pattern.compile("(?<=\\[)(.*?)(?=\\])");
    private static final Pattern FILTER_PATTERN = Pattern.compile("filter\\[(.*?)\\](?:\\[(.*?)\\])?");

    public record FilterCondition(TemplateField field, FilterOperator operator, String value) {}

    public record FilterConditions(List<FilterCondition> conditions) {
        public int size() {
            return conditions != null ? conditions.size() : 0;
        }
        public SelectionFilter.Builder toSelectionFilterBuilder() {
            SelectionFilter.Builder filterBuilder = SelectionFilter.builder();
            for(FilterCondition filterCondition : conditions) {
                switch(filterCondition.operator) {
                    case FilterOperator.STARTS_WITH:
                        filterBuilder.startsWith(filterCondition.value(), filterCondition.field());
                        break;
                    case FilterOperator.ENDS_WITH:
                        filterBuilder.endsWith(filterCondition.value(), filterCondition.field());
                        break;
                    case FilterOperator.EQUAL_TO:
                        filterBuilder.equalTo(filterCondition.value(), filterCondition.field());
                        break;
                    //case FilterOperator.IN:
                    //    for(String s : filterCondition.value().split(",")) {
                    //        //filterBuilder.equalTo(filterCondition.value(), filterCondition.field());
                    //    }
                    //    break;
                    default:
                        break;
                }
            }
            return filterBuilder;
        }
    }

    public static FilterConditions parse(Map<String, String[]> parameterMap) {
        List<FilterCondition> conditions = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            Matcher matcher = FILTER_PATTERN.matcher(entry.getKey());
            if (matcher.find()) {
                TemplateField templateField = TemplateField.fromValue(matcher.group(1).toUpperCase());
                FilterOperator operator = matcher.group(2) != null ? FilterOperator.fromKeyword(matcher.group(2)) : FilterOperator.EQUAL_TO;
                String value = entry.getValue()[0]; // TODO:  Check multiple values?
                conditions.add(new FilterCondition(templateField, operator, value));
            }
        }
        return new FilterConditions(conditions);
    }

    public static List<String> tokenize(String queryKey) {
        List<String> tokens = new ArrayList<>();
        if (queryKey.contains("[")) {
            tokens.add(queryKey.substring(0, queryKey.indexOf("[")));
        }
        Matcher matcher = BRACKET_PATTERN.matcher(queryKey);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

}
