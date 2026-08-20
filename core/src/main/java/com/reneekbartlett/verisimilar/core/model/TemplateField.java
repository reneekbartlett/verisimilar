package com.reneekbartlett.verisimilar.core.model;

import java.util.Date;
import java.util.EnumSet;

public enum TemplateField {
    FIRST_NAME("FIRST", String.class, null),
    MIDDLE_NAME("MIDDLE", String.class, null),
    LAST_NAME("LAST", String.class, null),
    NICKNAME("NICKNAME", String.class, null),

    BIRTHDAY("BIRTHDAY", Date.class, null),
    GENERATION("GENERATION", EnumSet.class, Generation.class),

    GENDER_IDENTITY("GENDER_IDENTITY", EnumSet.class, GenderIdentity.class),

    ETHNICITY("ETHNICITY", EnumSet.class, Ethnicity.class),

    //KEYWORD("KEYWORD", String.class, null),
    KEYWORD1("KEYWORD1", String.class, null),
    KEYWORD2("KEYWORD2", String.class, null),
    KEYWORD3("KEYWORD3", String.class, null),
    KEYWORD_TYPE("KEYWORD_TYPE", EnumSet.class, KeywordType.class),

    STREET_ID("STREET_ID", String.class, null),
    STREET_NAME("STREET_NAME", String.class, null),
    STREET_SUFFIX("STREET_SUFFIX", EnumSet.class, StreetSuffix.class),
    ADDRESS1("ADDRESS1", String.class, null),
    ADDRESS2("ADDRESS2", String.class, null),
    CITY("CITY", String.class, null),
    STATE("STATE", EnumSet.class, USState.class),
    ZIP_CODE("ZIP_CODE", String.class, null),
    REGION("REGION", EnumSet.class, USRegion.class),

    CITY_STATE_ZIP("CITY_STATE_ZIP", String.class, null),

    PHONE_NUMBER("PHONE_NUMBER", String.class, null),
    PHONE_NUMBER_TYPE("PHONE_NUMBER_TYPE", EnumSet.class, PhoneNumberType.class),
    AREA_CODE("AREA_CODE", String.class, null),

    EMAIL_ADDRESS("EMAIL_ADDRESS", String.class, null),
    // TODO:  EMAIL_ADDRESS_TYPE
    EMAIL_ADDRESS_TYPE("EMAIL_ADDRESS_TYPE", String.class, null),

    USERNAME("USERNAME", String.class, null),
    USERNAME_TYPE("USERNAME_TYPE", EnumSet.class, UsernameType.class),

    DOMAIN("DOMAIN", String.class, null),
    DOMAIN_TYPE("DOMAIN_TYPE", EnumSet.class, DomainType.class),

    ADDRESS_CATEGORY("ADDRESS_CATEGORY", EnumSet.class, AddressCategory.class),
    UNIT_TYPE("UNIT_TYPE", EnumSet.class, UnitType.class),
    UNIT_NUMBER("UNIT_NUMBER", Integer.class, null),
    UNIT_XTRA("UNIT_XTRA", String.class, null),

    SEPARATOR("SEPARATOR", String.class, null),

    NUM10("NUM10", Integer.class, null),
    NUM100("NUM100", Integer.class, null),
    NUM1000("NUM1000", Integer.class, null);

    /***
     * Placeholder used in the StringTemplate
     */
    private final String placeholder;

    private final Class<?> targetType;
    private final Class<? extends Enum<?>> enumType;

    // TODO:  Add field for storing applicable Generator Classes?
    private <T> TemplateField(String placeholder, Class<?> targetType, Class<? extends Enum<?>> enumType) {
        this.placeholder = placeholder;
        this.targetType = targetType;
        this.enumType = enumType;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getLabel() {
        return placeholder;
    }

    public Class<?> targetType() { return this.targetType; }
    public Class<? extends Enum<?>> enumType() { return this.enumType; }

    // Companion reflection method to tell the converter which elements belong in the Set
    public Class<? extends Enum> getEnumElementClass() {
        if (this == DOMAIN_TYPE)
            return DomainType.class; // Link directly to your sub-enum class

        if (this == USERNAME_TYPE) 
            return UsernameType.class;

        if (this == KEYWORD_TYPE)
            return KeywordType.class;

        if (this == UNIT_TYPE) 
            return UnitType.class;

        if (this == STATE)
            return USState.class;

        if (this == REGION)
            return USRegion.class;

        if (this == GENDER_IDENTITY)
            return GenderIdentity.class;

        if (this == GENERATION)
            return Generation.class;

        if (this == ETHNICITY)
            return Ethnicity.class;

        if (this == STREET_SUFFIX)
            return StreetSuffix.class;

        if (this == ETHNICITY)
            return Ethnicity.class;

        return null;
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

    public static boolean isEnumField(TemplateField field) {
        if(field.enumType != null) {
            return true;
        }
        return false;
    }
}
