package com.reneekbartlett.verisimilar.core.selector.filter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.model.PhoneNumberType;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.model.UnitType;
import com.reneekbartlett.verisimilar.core.model.UsernameType;
import com.reneekbartlett.verisimilar.core.model.WeightedEnumData;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter.Builder;

/***
 *  Enum mapping to the Builder functions to set individual field value or set of multiple values.
 */
public enum SelectionFieldMapper {
    FIRST_NAME(
        TemplateField.FIRST_NAME,
        (builder, value) -> builder.firstName((String) value),
        null
    ),
    MIDDLE_NAME(
        TemplateField.MIDDLE_NAME,
        (builder, value) -> builder.middleName((String) value),
        null
    ),
    LAST_NAME(
        TemplateField.LAST_NAME, 
        (builder, value) -> builder.lastName((String) value),
        null
    ),
    NICKNAME(
        TemplateField.NICKNAME, 
        (builder, value) -> builder.nickName((String) value),
        null
    ),
    BIRTHDAY(
        TemplateField.BIRTHDAY,
        // TODO: convert
        (builder, value) -> builder.birthday((String) value),
        null
    ),
    GENDER_IDENTITY(
        TemplateField.GENDER_IDENTITY, 
        (builder, value) -> builder.gender((GenderIdentity) value),
        (builder, values) -> builder.genders(castEnumSet(values, GenderIdentity.class))
    ),
    GENERATION(
        TemplateField.GENERATION, 
        (builder, value) -> builder.generation((Generation) value),
        (builder, values) -> builder.generations(castEnumSet(values, Generation.class))
    ),
    ETHNICITY(
        TemplateField.ETHNICITY, 
        (builder, value) -> builder.ethnicity((Ethnicity) value),
        (builder, values) -> builder.ethnicities(castEnumSet(values, Ethnicity.class))
    ),
    CITY(
         TemplateField.CITY, 
        (builder, value) -> builder.city((String) value),
        null
    ),
    STATE(
        TemplateField.STATE, 
        (builder, value) -> builder.state((USState) value),
        //(builder, values) -> builder.states((Set<USState>) values)
        (builder, values) -> builder.states(castEnumSet(values, USState.class))
    ),
    ZIP_CODE(
        TemplateField.ZIP_CODE, 
        (builder, value) -> builder.zipCode((String) value),
        //(builder, values) -> builder.zipCodes((Set<String>) values)
        (builder, values) -> builder.zipCodes(castStringSet(values))
    ),
    CITY_STATE_ZIP(
        TemplateField.CITY_STATE_ZIP, 
        (builder, value) -> builder.cityStateZip((CityStateZip) value),
        null
    ),
    UNIT_TYPE(
        TemplateField.UNIT_TYPE, 
        (builder, value) -> builder.unitType((UnitType) value), // TODO
        //(builder, values) -> builder.unitTypes((Set<UnitType>) values)
        null
    ),
    UNIT_NUMBER(
        TemplateField.UNIT_NUMBER, 
        (builder, value) -> builder.unitNumber((String) value),
        null
    ),
    REGION(
        TemplateField.REGION, 
        (builder, value) -> builder.region((USRegion) value), // TODO
        //(builder, values) -> builder.regions((Set<USRegion>) values)
        null
    ),
    USERNAME(
        TemplateField.USERNAME, 
        (builder, value) -> builder.username((String) value),
        //(builder, values) -> builder.usernames((Set<String>) values)
        null
    ),
    USERNAME_TYPE(
        TemplateField.USERNAME_TYPE, 
        (builder, value) -> builder.usernameType((UsernameType) value), // TODO
        //(builder, values) -> builder.usernameTypes((Set<UsernameType>) values)
        null
    ),
    DOMAIN(
        TemplateField.DOMAIN, 
        (builder, value) -> builder.domain((String) value),
        (builder, values) -> builder.domains(castStringSet(values))
    ),
    DOMAIN_TYPE(
        TemplateField.DOMAIN_TYPE, 
        (builder, value) -> {
            return builder.domainType(DomainType.fromValue(value.toString())); 
        },
        (builder, values) -> {
            return builder.domainTypes(DomainType.fromValues(castStringSet(values))); 
        }
    ),
    EMAIL_ADDRESS(
        TemplateField.EMAIL_ADDRESS, 
        //(builder, value) -> builder.emailAddress((String) value),
        null,
        //(builder, values) -> builder.emailAddresses((Set<String>) values)
        null
    ),
    EMAIL_ADDRESS_TYPE(
        TemplateField.EMAIL_ADDRESS_TYPE, 
        //(builder, value) -> builder.emailAddressType((EmailAddressType) value),
        null,
        //(builder, values) -> builder.emailAddressTypes((Set<EmailAddressType>) values)
        null
    ),
    PHONE_NUMBER(
        TemplateField.PHONE_NUMBER, 
        (builder, value) -> builder.phoneNumber((String) value),
        //(builder, values) -> builder.phoneNumbers((Set<String>) values)
        null
    ),
    PHONE_NUMBER_TYPE(
        TemplateField.PHONE_NUMBER_TYPE, 
        (builder, value) -> builder.phoneNumberType((PhoneNumberType) value),
        //(builder, values) -> builder.phoneNumberTypes((Set<PhoneNumberType>) values)
        null
    ),
    KEYWORD_TYPE(
        TemplateField.KEYWORD_TYPE, 
        null,
        null
    );

    protected static final Logger LOGGER = LoggerFactory.getLogger(SelectionFieldMapper.class);

    private final TemplateField templateField;
    private final BiFunction<Builder, Object, Builder> singleConsumer;
    private final BiFunction<Builder, Set<?>, Builder> multiConsumer;

    // Static lookup cache for fast, non-loop O(1) performance
    static final Map<TemplateField, SelectionFieldMapper> LOOKUP = Arrays.stream(values())
            .collect(Collectors.toMap(SelectionFieldMapper::getTemplateField, Function.identity()));

    SelectionFieldMapper(
            TemplateField templateField, 
            BiFunction<Builder, Object, Builder> singleConsumer, 
            BiFunction<Builder, Set<?>, Builder> multiConsumer
    ) {
        this.templateField = templateField;
        this.singleConsumer = singleConsumer;
        this.multiConsumer  = multiConsumer;
    }

    public TemplateField getTemplateField() { return templateField; }

    public Builder applySingle(Builder builder, Object value) {
        if (singleConsumer  == null) {
            LOGGER.warn("UnsupportedOperationException");
            throw new UnsupportedOperationException("Single-value operator not supported for field: " + templateField);
        }
        LOGGER.debug("applySingle");

        // TODO:  Check type
        return this.singleConsumer.apply(builder, value);
    }

    public Builder applyMulti(Builder builder, Set<?> values) {
        if (multiConsumer  == null) {
            throw new UnsupportedOperationException("Multi-value operator not supported for field: " + templateField);
        }
        LOGGER.debug("applyMulti");
        return this.multiConsumer.apply(builder, values);
    }

    public static SelectionFieldMapper fromTemplateField(TemplateField field) {
        return LOOKUP.get(field);
    }

    /** 
     * Helper: return any value as Object (single or multi).
     */
    public Object asObject(Object value) {
        return value;
    }

    /** Safe conversion helpers */
    public static Set<String> castStringSet(Set<?> raw) {
        Set<String> result = new HashSet<>();
        for (Object o : raw) {
            result.add((String) o);
        }
        return result;
    }

    public static <E extends Enum<E>> EnumSet<E> castEnumSet(Set<?> raw, Class<E> enumType) {
        EnumSet<E> set = EnumSet.noneOf(enumType);
        for (Object o : raw) {
            set.add(enumType.cast(o));
        }
        return set;
    }

    public static <T extends Enum<T>> Set<String> toEnumNameSet(Set<?> enumValues) {
        Set<String> names = new HashSet<>();
        for (Object o : enumValues) {
            if (o instanceof Enum<?> e) {
                names.add(e.name());
            } else {
                //throw new IllegalArgumentException("Value is not an enum: " + o);
            }
        }
        return names;
    }

    public static Object getFirstValue(Set<?> set) {
        if (set == null || set.isEmpty()) {
            return null; // Or throw an exception depending on your needs
        }
        return set.iterator().next();
    }

    public static <T extends Enum<T>> String toEnumName(Set<?> enumValue) {
        Object o = getFirstValue(enumValue);
        if (o instanceof Enum<?> e) {
            return e.name();
        }
        return null;
    }

    /***
     * 
     * @param <E>
     * @param value
     * @return
     */
    public static <E extends Enum<E>> String extractLabel(E value) {
        if (value instanceof WeightedEnumData weightedEnumValue) {
            return weightedEnumValue.getLabel();
        }
        return value.name();
    }
    
    public static String normalizeEnum(Object value) {
        if (value instanceof WeightedEnumData weightedEnumValue) {
            return weightedEnumValue.getLabel().toUpperCase();
        }
        return value.toString().toUpperCase();
    }

    public static <E extends Enum<E>> Set<String> toLabels(Set<E> values) {
        return values.stream()
            .map(v -> v instanceof WeightedEnumData w ? w.getLabel() : v.name())
            .filter(label -> !label.isBlank())
            .collect(Collectors.toSet());
    }
}
