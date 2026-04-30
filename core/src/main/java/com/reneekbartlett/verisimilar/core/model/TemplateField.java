package com.reneekbartlett.verisimilar.core.model;

public enum TemplateField {
    FIRST_NAME("FIRST"),
    MIDDLE_NAME("MIDDLE"),
    LAST_NAME("LAST"),
    BIRTHDAY("BIRTHDAY"),
    NICKNAME("NICKNAME"),

    GENDER_IDENTITY("GENDER_IDENTITY"),

    ETHNICITY("ETHNICITY"),

    //KEYWORD("KEYWORD"),
    KEYWORD1("KEYWORD1"),
    KEYWORD2("KEYWORD2"),
    KEYWORD3("KEYWORD3"),

    STREET_NAME("STREET_NAME"),
    STREET_SUFFIX("STREET_SUFFIX"),
    ADDRESS2("ADDRESS2"),
    CITY("CITY"),
    STATE("STATE"),
    ZIP_CODE("ZIP_CODE"),
    REGION("REGION"),

    AREA_CODE("AREA_CODE"),
    USERNAME("USERNAME"),

    DOMAIN("DOMAIN"),
    DOMAIN_TYPE("DOMAIN_TYPE"),

    ADDRESS_CATEGORY("ADDRESS_CATEGORY"),
    UNIT_TYPE("UNIT_TYPE"),

    SEPARATOR("SEPARATOR"),

    NUM10("NUM10"),
    NUM100("NUM100"),
    NUM1000("NUM1000");

    /***
     * Placeholder used in the StringTemplate
     */
    private final String placeholder;

    // TODO:  Add field for storing applicable Generator Classes?
    private TemplateField(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getLabel() {
        return placeholder;
    }

    public static TemplateField fromValue(String templateValue) {
        if(templateValue != null) {
            for (TemplateField field : values()) {
                if (field.name().equalsIgnoreCase(templateValue)) {
                    return field;
                }
            }
        }
        //throw new IllegalArgumentException("Invalid state abbreviation: " + abbr);
        return null;
    }
}
