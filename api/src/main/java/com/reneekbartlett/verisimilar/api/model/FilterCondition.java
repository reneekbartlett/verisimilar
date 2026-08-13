package com.reneekbartlett.verisimilar.api.model;

import com.reneekbartlett.verisimilar.core.model.TemplateField;

public record FilterCondition(
        TemplateField field, 
        FilterOperator operator, 
        String filterValue // TODO:  Change to Object?
    ) {

    public FilterCondition(String field, String operator) {
        this(TemplateField.fromValue(field), FilterOperator.fromKeyword(operator), null);
    }

    public FilterCondition(String field, String operator, String value) {
        this(TemplateField.fromValue(field), FilterOperator.fromKeyword(operator), value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(0);
        sb.append("field=" + field != null ? field.getLabel() : "");
        sb.append(", operator=" + operator != null ? operator.keyword() : "");
        sb.append(", filterValue=" + filterValue != null ? filterValue : "");
        return sb.toString();
    }
}
