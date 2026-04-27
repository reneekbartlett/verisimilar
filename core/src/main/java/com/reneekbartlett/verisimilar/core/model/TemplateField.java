package com.reneekbartlett.verisimilar.core.model;

public enum TemplateField {
    FIRST_NAME("FIRST"),
    MIDDLE_NAME("MIDDLE"),
    LAST_NAME("LAST"),
    BIRTHDAY("BIRTHDAY"),
    KEYWORD("KEYWORD"),
    KEYWORD1("KEYWORD1"),

    STREET_NAME("STREET_NAME"),
    STREET_SUFFIX("STREET_SUFFIX"),
    CITY("CITY"),
    STATE("STATE"),
    AREA_CODE("AREA_CODE"),
    USERNAME("USERNAME"),
    DOMAIN("DOMAIN"),
    DOMAIN_TYPE("DOMAIN_TYPE"),

    ADDRESS_CATEGORY("ADDRESS_CATEGORY"),
    UNIT_TYPE("UNIT_TYPE"),

    NUM10("NUM10"),
    NUM100("NUM100"),
    NUM1000("NUM1000");

    /***
     * Placeholder used in the StringTemplate
     */
    private final String placeholder;

    private TemplateField(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
