package com.reneekbartlett.verisimilar.api.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.KeywordType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.selector.filter.InternalFilter;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.api.model.FilterOperator;

public class JsonApiParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonApiParser.class);

    private static final Pattern BRACKET_PATTERN = Pattern.compile("(?<=\\[)(.*?)(?=\\])");
    private static final Pattern FILTER_PATTERN = Pattern.compile("filter\\[(.*?)\\](?:\\[(.*?)\\])?");

    //private EnumSet<TemplateField> returnFields;

    public record FilterCondition(TemplateField field, FilterOperator operator, String value) {}

    public record FilterConditions(List<FilterCondition> conditions) {
        public int size() {
            return conditions != null ? conditions.size() : 0;
        }

        public SelectionFilter.Builder toSelectionFilterBuilder() {
            EnumSet<TemplateField> stringFields = TemplateField.stringFields();
            EnumSet<TemplateField> enumFields = TemplateField.enumFields();

            SelectionFilter.Builder filterBuilder = SelectionFilter.builder();
            InternalFilter.Builder internalFilterBuilder = InternalFilter.getBuilder();
            for(FilterCondition filterCondition : conditions) {
                switch(filterCondition.operator) {
                    case FilterOperator.STARTS_WITH:
                        filterBuilder.startsWith(filterCondition.value(), filterCondition.field());
                        internalFilterBuilder.startsWith(filterCondition.value(), filterCondition.field());
                        break;
                    case FilterOperator.ENDS_WITH:
                        // TODO:  check if endsWithMap contains key for TemplateField
                        filterBuilder.endsWith(filterCondition.value(), filterCondition.field());
                        internalFilterBuilder.endsWith(filterCondition.value(), filterCondition.field());
                        break;
                    
                    case FilterOperator.CONTAINS:
                        // TODO:  check if containsMap contains key for TemplateField
                        filterBuilder.contains(filterCondition.value(), filterCondition.field());
                        internalFilterBuilder.contains(filterCondition.value(), filterCondition.field());
                        break;
                    case FilterOperator.EQUAL_TO:
                        // TODO:  check if equalToMap contains key for TemplateField
                        // 
                        //filterBuilder.equalTo(filterCondition.value(), filterCondition.field());
                        
                        internalFilterBuilder.equalTo(filterCondition.value(), filterCondition.field());
                        break;
                    case FilterOperator.IN:
                        TemplateField field = filterCondition.field();
                        String[] valArray = filterCondition.value().split(",");
                        if(enumFields.contains(field)) {
                            // KEYWORD_TYPE, DOMAIN_TYPE, USERNAME_TYPE
                            // ADDRESS_CATEGORY, UNIT_TYPE
                            // USREGION
                            if(field.equals(TemplateField.STATE)) {
                                EnumSet<USState> states = USState.convertToEnumSet(Set.of(valArray));
                                filterBuilder.states(states);
                            } else if(field.equals(TemplateField.GENDER_IDENTITY)) {
                                EnumSet<GenderIdentity> genders = GenderIdentity.convertToEnumSet(Set.of(valArray));
                                filterBuilder.genders(genders);
                            } else if(field.equals(TemplateField.KEYWORD_TYPE)) {
                                //EnumSet<KeywordType> keywordTypes = KeywordType.convertToEnumSet(Set.of(valArray));
                                //filterBuilder.keywordTypes(keywordTypes);
                            } else if(field.equals(TemplateField.GENERATION)) {
                                //filterBuilder
                            }
                        } else if(stringFields.contains(field)) {
                            Set<String> strVals = Set.of(valArray);
                            filterBuilder.in(strVals, field);
                            internalFilterBuilder.in(strVals, field);
                        }
                        break;
                    default:
                        break;
                }
            }
            //return filterBuilder;
            return internalFilterBuilder;
        }
    }

    private final List<TemplateField> filterFields;

    public JsonApiParser(List<TemplateField> filterFields) {
        this.filterFields = filterFields;
    }

    public FilterConditions parse(Map<String, String[]> parameterMap) {
        return parse(parameterMap, this.filterFields);
    }

    public static FilterConditions parse(Map<String, String[]> parameterMap, List<TemplateField> filterFields) {
        List<FilterCondition> conditions = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            Matcher matcher = FILTER_PATTERN.matcher(entry.getKey());
            if (matcher.find()) {
                TemplateField templateField = TemplateField.fromValue(matcher.group(1).toUpperCase());
                if(templateField == null || !filterFields.contains(templateField)) {
                    LOGGER.debug("TemplateField not supported: {}", templateField);
                    continue;
                }

                FilterOperator operator = FilterOperator.fromKeyword(matcher.group(2));
                if(operator == null) {
                    LOGGER.debug("Operator not supported ({}).  Defaulting to EQUAL_TO.", matcher.group(2));
                    operator = FilterOperator.EQUAL_TO;
                }

                if(entry.getValue().length >=1) {
                    String value = entry.getValue()[0]; // TODO:  Check multiple values?
                    conditions.add(new FilterCondition(templateField, operator, value));
                }
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
