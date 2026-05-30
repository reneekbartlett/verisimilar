package com.reneekbartlett.verisimilar.core.model;

import java.util.Date;
import java.util.EnumSet;

public enum TemplateField {
    FIRST_NAME("FIRST", String.class),
    MIDDLE_NAME("MIDDLE", String.class),
    LAST_NAME("LAST", String.class),
    BIRTHDAY("BIRTHDAY", Date.class),
    NICKNAME("NICKNAME", String.class),

    GENDER_IDENTITY("GENDER_IDENTITY", Enum.class),

    ETHNICITY("ETHNICITY", Enum.class),

    GENERATION("GENERATION", Enum.class),

    //KEYWORD("KEYWORD", String.class),
    KEYWORD1("KEYWORD1", String.class),
    KEYWORD2("KEYWORD2", String.class),
    KEYWORD3("KEYWORD3", String.class),
    KEYWORD_TYPE("KEYWORD_TYPE", Enum.class),

    STREET_NAME("STREET_NAME", String.class),
    STREET_SUFFIX("STREET_SUFFIX", String.class),
    ADDRESS2("ADDRESS2", String.class),
    CITY("CITY", String.class),
    STATE("STATE", Enum.class),
    ZIP_CODE("ZIP_CODE", String.class),
    REGION("REGION", Enum.class),

    AREA_CODE("AREA_CODE", String.class),
    USERNAME("USERNAME", String.class),
    USERNAME_TYPE("USERNAME_TYPE", Enum.class),

    DOMAIN("DOMAIN", String.class),
    DOMAIN_TYPE("DOMAIN_TYPE", Enum.class),

    ADDRESS_CATEGORY("ADDRESS_CATEGORY", Enum.class),
    UNIT_TYPE("UNIT_TYPE", Enum.class),

    SEPARATOR("SEPARATOR", String.class),

    NUM10("NUM10", Integer.class),
    NUM100("NUM100", Integer.class),
    NUM1000("NUM1000", Integer.class);

    /***
     * Placeholder used in the StringTemplate
     */
    private final String placeholder;
    
    private final Class clazz;

    // TODO:  Add field for storing applicable Generator Classes?
    private <T> TemplateField(String placeholder, Class<T> clazz) {
        this.placeholder = placeholder;
        this.clazz = clazz;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getLabel() {
        return placeholder;
    }

    public <T> Class<T> getType() {
        return clazz;
    }

    public static TemplateField fromValue(String value) {
        if(value != null) {
            for (TemplateField field : EnumSet.allOf(TemplateField.class)) {
                if (field.name().equalsIgnoreCase(value) || field.getLabel().equalsIgnoreCase(value)) {
                    return field;
                }
            }
        }
        return null;
    }

    // TODO:  Use clazz?
    public static EnumSet<TemplateField> stringFields(){
        return EnumSet.of(FIRST_NAME, 
                MIDDLE_NAME, 
                LAST_NAME,
                NICKNAME,
                USERNAME, 
                STREET_NAME,
                CITY,
                AREA_CODE,
                KEYWORD1, KEYWORD2, KEYWORD3
        );
    }

    public static EnumSet<TemplateField> dateFields(){
        return EnumSet.of(BIRTHDAY);
    }

    public static EnumSet<TemplateField> enumFields(){
        return EnumSet.of(STATE, GENDER_IDENTITY,
                ETHNICITY, REGION,
                USERNAME_TYPE,
                DOMAIN_TYPE,
                UNIT_TYPE
        );
    }
}
