package com.reneekbartlett.verisimilar.core.selector.filter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.reneekbartlett.verisimilar.core.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.parquet.column.ValuesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 
 */
public record SelectionFilter(
        Optional<String> firstName,
        Optional<String> middleName,
        Optional<String> lastName,

        Optional<String> nickName,

        Optional<GenderIdentity> gender,
        Optional<Set<GenderIdentity>> genders,

        Optional<LocalDate> birthday,
        Optional<Generation> generation,
        Optional<Set<Generation>> generations,
        Optional<Integer> minYear,
        Optional<Integer> maxYear,

        Optional<AddressCategory> addressCategory,
        Optional<UnitType> unitType,

        Optional<String> streetName,
        Optional<String> streetSuffix,

        Optional<String> address2,

        Optional<String> city,

        Optional<USState> state,
        Optional<Set<USState>> states,

        Optional<String> zipCode,
        Optional<Set<String>> zipCodes,

        Optional<USRegion> region,

        Optional<Ethnicity> ethnicity,

        Optional<DomainType> domainType,
        Optional<String> domain,

        Optional<UsernameType> usernameType,
        Optional<String> username,

        Optional<PhoneNumberType> phoneNumberType,
        Optional<String> areaCode,

        Optional<SelectionPredicate<String>> customPredicate,
        Optional<Set<SelectionPredicate<String>>> customPredicates,

        Map<TemplateField, String> startsWithMap,
        Map<TemplateField, String> endsWithMap,
        Map<TemplateField, String> equalToMap,
        Map<TemplateField, String> containsMap,
        Map<TemplateField, Set<String>> inMap,
        Map<TemplateField, Set<?>> inEnumMap
) {

    private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
            .toFormatter();

    public Set<String> getDomains(){
        return HashSet.newHashSet(0);
    }

    public Optional<DomainRecord> domainRecord(){
        if(domain.isPresent() && domainType.isPresent()) {
            return Optional.of(new DomainRecord(domain.get(), domainType.get()));
        }
        return Optional.empty();
    }

    public Optional<AddressCategory> addressCategory(){
        String addressCategoryFilter = this.equalToMap().get(TemplateField.ADDRESS_CATEGORY);
        if(addressCategoryFilter != null) {
            return Optional.of(AddressCategory.fromLabel(addressCategoryFilter));
        }
        return Optional.empty();
    }

    public Optional<String> streetId(){
        String streetIdFilter = this.equalToMap().get(TemplateField.STREET_ID);
        if(streetIdFilter != null) {
            return Optional.of(streetIdFilter);
        }
        return Optional.empty();
    }

    public Optional<UnitType> unitType(){
        String unitTypeFilter = this.equalToMap().get(TemplateField.UNIT_TYPE);
        if(unitTypeFilter != null) {
            return Optional.of(UnitType.fromLabel(unitTypeFilter));
        }
        return Optional.empty();
    }

    public Optional<String> unitNumber(){
        String unitTypeFilter = this.equalToMap().get(TemplateField.UNIT_NUMBER);
        if(unitTypeFilter != null) {
            return Optional.of(unitTypeFilter);
        }
        return Optional.empty();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectionFilter.class);

    public SelectionFilter {
        // TODO:  Check if these optional checks are necessary
        firstName = firstName == null? Optional.empty() : firstName;
        middleName = middleName == null? Optional.empty() : middleName;
        lastName = lastName == null? Optional.empty() : lastName;

        nickName = nickName == null? Optional.empty() : nickName;

        gender = gender == null ? Optional.empty() : gender;
        genders = genders == null ? Optional.empty() : genders;

        birthday = (birthday == null) ? Optional.empty() : birthday;

        generation = (generation == null) ? Optional.empty() : generation;
        generations = (generations == null) ? Optional.empty() : generations;

        minYear = (minYear == null) ? Optional.empty() : minYear;
        maxYear = (maxYear == null) ? Optional.empty() : maxYear;

        addressCategory = addressCategory == null ? Optional.empty() : addressCategory;
        unitType = unitType == null ? Optional.empty() : unitType;

        streetName = streetName == null ? Optional.empty() : streetName;
        streetSuffix = streetSuffix == null ? Optional.empty() : streetSuffix;

        address2 = address2 == null ? Optional.empty() : address2;

        city = city == null ? Optional.empty() : city;

        state = state == null ? Optional.empty() : state;
        states = states == null ? Optional.empty() : states;

        zipCode = zipCode == null ? Optional.empty() : zipCode;
        zipCodes = zipCodes == null ? Optional.empty() : zipCodes;

        region = region == null ? Optional.empty() : region;
        ethnicity = ethnicity == null ? Optional.empty() : ethnicity;

        phoneNumberType = phoneNumberType == null ? Optional.empty() : phoneNumberType;
        areaCode = areaCode == null ? Optional.empty() : areaCode;

        domainType = domainType == null ? Optional.empty() : domainType;
        domain = domain == null ? Optional.empty() : domain;

        usernameType = usernameType == null ? Optional.empty() : usernameType;
        username = username == null ? Optional.empty() : username;

        customPredicate = customPredicate == null ? Optional.empty() : customPredicate;
        customPredicates = customPredicates == null ? Optional.empty() : customPredicates;
    }

    public static Builder toBuilder(SelectionFilter filter) {
        if(filter == null) {
            return new Builder();
        }
        return new Builder(filter);
    }

    //private Builder toBuilder() {
    //    // todo: clone?
    //    return new Builder(this);
    //}

    public PersonRecord getPersonRecord() {
        FullName fullName = new FullName(firstName.orElse(""), middleName.orElse(""), lastName.orElse(""));
        GenderIdentity genderIdentity = gender.orElse(GenderIdentity.GENDER_UNSPECIFIED);
        return new PersonRecord(
                fullName,
                genderIdentity,
                birthday.orElse(null)
        );
    }

//    public Map<TemplateField, Object> getResolvedValues() {
//        ConcurrentHashMap<TemplateField, Object> values = new ConcurrentHashMap<>();
//        if(!firstName.isEmpty()) values.put(TemplateField.FIRST_NAME, firstName.get());
//        if(!middleName.isEmpty()) values.put(TemplateField.MIDDLE_NAME, middleName.get());
//        if(!lastName.isEmpty()) values.put(TemplateField.LAST_NAME, lastName.get());
//        if(!birthday.isEmpty()) {
//            values.put(TemplateField.BIRTHDAY, birthday.get());
//        }
//
//        if(!domain.isEmpty()) values.put(TemplateField.DOMAIN, domain.get());
//        if(!gender.isEmpty()) values.put(TemplateField.GENDER_IDENTITY, gender.get());
//        return values;
//    }

    public boolean isEmpty() {
        return customPredicate.isEmpty() 
                && customPredicates.isEmpty()
                && firstName.isEmpty() && middleName.isEmpty() && lastName.isEmpty()
                && nickName.isEmpty()
                && gender.isEmpty()
                && birthday.isEmpty()
                && generation.isEmpty()
                && minYear.isEmpty()
                && maxYear.isEmpty()
                && streetName.isEmpty()
                && streetSuffix.isEmpty()
                && address2.isEmpty()
                && city.isEmpty()
                && states.isEmpty()
                && state.isEmpty()
                && zipCode.isEmpty()
                && zipCodes.isEmpty()
                && region.isEmpty()
                && ethnicity.isEmpty()
                && usernameType.isEmpty()
                && username.isEmpty()
                && domainType.isEmpty()
                && domain.isEmpty()
                && phoneNumberType.isEmpty()
                && areaCode.isEmpty()
                && (startsWithMap != null && startsWithMap.isEmpty())
                && (endsWithMap != null && endsWithMap.isEmpty())
                && (equalToMap != null && equalToMap.isEmpty())
                && (containsMap != null && containsMap.isEmpty())
                && (inMap != null && inMap.isEmpty())
                && (inEnumMap != null && inEnumMap.isEmpty());
    }

    public static SelectionFilter empty() {
        return new SelectionFilter(
                // FullName
                Optional.empty(), 
                Optional.empty(), 
                Optional.empty(),

                Optional.empty(), // nickName

                Optional.empty(), // Gender
                Optional.empty(), // Genders

                Optional.empty(), // Birthday
                Optional.empty(), // Generation
                Optional.empty(), // Generations
                Optional.empty(), Optional.empty(), // MinYear, MaxYear

                //Optional.empty(), // PostalAddress

                Optional.empty(), // AddressCategory
                Optional.empty(), // UnitType
                
                Optional.empty(), // streetName
                Optional.empty(), // streetSuffix

                Optional.empty(), // address2

                Optional.empty(), // city

                Optional.empty(), // state
                Optional.empty(), // states

                Optional.empty(), //zipCode
                Optional.empty(), // zipCodes
                Optional.empty(), // region
                Optional.empty(), // ethnicity

                Optional.empty(), // domainType
                Optional.empty(), // domain

                Optional.empty(), // usernameType
                Optional.empty(), // username

                Optional.empty(), // phoneNumberType
                Optional.empty(), // areaCode

                Optional.empty(), // customPredicate
                Optional.empty(), // customPredicates

                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>() // inEnumMap
        );
    }

    // ------------------------------------------------------------
    // Builder
    // ------------------------------------------------------------
    //
    // TODO:  Decide if I want to keep individual fields are just use the maps (i.e. equalToMap, etc.)
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String firstName;
        private String middleName;
        private String lastName;
        private String nickName;

        private GenderIdentity gender;
        private Set<GenderIdentity> genders;

        private LocalDate birthday;
        private Generation generation;
        private Set<Generation> generations;
        private Integer minYear;
        private Integer maxYear;

        private AddressCategory addressCategory;
        private UnitType unitType;

        private String streetName;
        private String streetSuffix;
        private String address2;

        private String city;

        private USState state;
        private Set<USState> states;

        private String zipCode;
        private Set<String> zipCodes;

        private USRegion region;
        private Ethnicity ethnicity;

        private DomainType domainType;
        private String domain;

        private UsernameType usernameType;
        private String username;

        private PhoneNumberType phoneNumberType;
        private String areaCode;

        protected SelectionPredicate<String> customPredicate;
        protected final Set<SelectionPredicate<String>> customPredicates = ConcurrentHashMap.newKeySet();

        protected final Map<TemplateField, String> startsWithMap = new ConcurrentHashMap<>();
        protected final Map<TemplateField, String> endsWithMap = new ConcurrentHashMap<>();

        protected final Map<TemplateField, String> containsMap = new ConcurrentHashMap<>();

        protected final Map<TemplateField, String> equalToMap = new ConcurrentHashMap<>();
        protected final Map<TemplateField, Set<String>> inMap = new ConcurrentHashMap<>();
        protected final Map<TemplateField, Set<?>> inEnumMap = new ConcurrentHashMap<>();

        protected CityStateZip cityStateZip;

        public Builder() {
            //
        }

        @SuppressWarnings("unchecked")
        public <T> Set<T> getInEnumSet(TemplateField field, Class<T> type) {
            return (Set<T>) inEnumMap.computeIfAbsent(field, k -> new HashSet<T>());
        }

        /***
         * Copy over some existing elements.
         */
        public Builder(SelectionFilter filter) {
            if(filter == null) {
                return;
            }

            this.gender = filter.gender.orElse(null);
            this.ethnicity = filter.ethnicity.orElse(null);

            this.firstName = filter.firstName.orElse(null);
            this.middleName = filter.middleName.orElse(null);
            this.lastName = filter.lastName.orElse(null);
            this.nickName = filter.nickName.orElse(null);

            this.birthday = filter.birthday.orElse(null);
            this.generation = filter.generation.orElse(null);
            this.generations = filter.generations.orElse(null);

            // AddressLineOne
            this.addressCategory = filter.addressCategory.orElse(null);
            this.unitType = filter.unitType.orElse(null);

            this.streetName = filter.streetName.orElse(null);
            this.streetSuffix = filter.streetSuffix.orElse(null);

            // AddressLineTwo
            this.address2 = filter.address2.orElse(null);

            this.city = filter.city.orElse(null);

            this.state = filter.state.orElse(null);
            this.states = filter.states.orElse(null);

            this.zipCode = filter.zipCode.orElse(null);
            this.zipCodes = filter.zipCodes.orElse(null);

            this.region = filter.region.orElse(null);

            this.phoneNumberType = filter.phoneNumberType.orElse(null);
            this.areaCode = filter.areaCode.orElse(null);

            this.username = filter.username.orElse(null);
            this.usernameType = filter.usernameType.orElse(null);

            this.domain = filter.domain.orElse(null);
            this.domainType = filter.domainType.orElse(null);

            filter.startsWithMap().forEach((k,v) -> this.startsWithMap.putIfAbsent(k, v));
            filter.endsWithMap().forEach((k,v) -> this.endsWithMap.putIfAbsent(k, v));
            filter.containsMap().forEach((k,v) -> this.containsMap.putIfAbsent(k, v));

            filter.equalToMap().forEach((k,v) -> this.equalToMap.putIfAbsent(k, v));
            filter.inMap().forEach((k,v) -> this.inMap.putIfAbsent(k, v));
            filter.inEnumMap().forEach((k,v) -> this.inEnumMap.putIfAbsent(k, v));

            //LOGGER.debug(this.toString());
        }

        public Builder firstName(String value) {
            if(value != null) {
                this.firstName = value;
                this.equalToMap.putIfAbsent(TemplateField.FIRST_NAME, value);
            }
            return this;
        }

        public Builder middleName(String value) {
            if(value != null) {
                this.middleName = value;
                this.equalToMap.putIfAbsent(TemplateField.MIDDLE_NAME, value);
            }
            return this;
        }

        public Builder lastName(String value) {
            if(value != null) {
                this.lastName = value;
                this.equalToMap.putIfAbsent(TemplateField.LAST_NAME, value);
            }
            return this;
        }

        public Builder nickName(String value) {
            if(value != null) {
                this.nickName = value;
                this.equalToMap.putIfAbsent(TemplateField.NICKNAME, value);
            }
            return this;
        }

        public Builder gender(GenderIdentity value) {
            if(value != null) {
                this.gender = value;
                this.equalToMap.putIfAbsent(TemplateField.GENDER_IDENTITY, value.getLabel());
            }
            return this;
        }

        public Builder genders(Set<GenderIdentity> values) {
            if (values == null || values.isEmpty()) return this;

            this.genders = values;
            this.inEnumMap.putIfAbsent(TemplateField.GENDER_IDENTITY, values);
            return this;
        }

        public Builder birthday(LocalDate value) {
            if(value != null) {
                this.birthday = value;
            }
            return this;
        }

        // TODO:  Move..
        public Builder birthday(String value) {
            if(value != null) {
                try {
                    LocalDate parsedDate = LocalDate.parse(value, DATE_FORMATTER);
                    this.birthday = parsedDate;
                } catch (Exception e) {
                    //
                }
            }
            return this;
        }

        public Builder generation(Generation value) {
            if(value != null) {
                this.generation = value;
            }
            return this;
        }

        public Builder generations(Set<Generation> values) {
            if(values != null) {
                this.generations = values;
            }
            return this;
        }

        public Builder minYear(Integer value) {
            if(value != null) {
                this.minYear = value;
            }
            return this;
        }

        public Builder maxYear(Integer value) {
            if(value != null) {
                this.maxYear = value;
            }
            return this;
        }

        public Builder postalAddress(PostalAddress value) {
            if(value != null 
                    && value.city() != null && value.state() != null && value.zip() != null) {
                return this.city(value.city())
                    .state(USState.fromAbbreviation(value.state()))
                    .zipCode(value.zip());
            }
            return this;
        }

        public Builder addressCategory(AddressCategory value) {
            if(value == null) return this;
            this.addressCategory = value;
            this.inEnumMap.putIfAbsent(TemplateField.ADDRESS_CATEGORY, EnumSet.of(value));
            return this;
        }

        public Builder unitType(UnitType value) {
            if(value == null) return this;
            this.unitType = value;
            this.inEnumMap.putIfAbsent(TemplateField.UNIT_TYPE, EnumSet.of(value));
            return this;
        }

        public Builder streetName(String value) {
            if(value == null) return this;
            this.streetName = value;
            this.equalToMap.putIfAbsent(TemplateField.STREET_NAME, value);
            return this;
        }

        public Builder streetSuffix(String value) {
            if(value == null) return this;
            this.streetSuffix = value;
            this.equalToMap.putIfAbsent(TemplateField.STREET_SUFFIX, value);
            return this;
        }

        public Builder address2(AddressLineTwo value) {
            if(value != null) {
                this.address2 = value.toString();
                this.equalToMap.putIfAbsent(TemplateField.ADDRESS2, value.toString());
                this.equalToMap.putIfAbsent(TemplateField.UNIT_NUMBER, value.unitNumber());
                this.equalToMap.putIfAbsent(TemplateField.UNIT_TYPE, value.unitType().getLabel());
            }
            return this;
        }

        public Builder city(String value) {
            if(value != null) {
                this.city = value;
                this.equalToMap.putIfAbsent(TemplateField.CITY, value);
                LOGGER.debug(this.city);
                Set<String> cityNames = Set.of(city+"$");
                this.startsWithMap.putIfAbsent(TemplateField.CITY_STATE_ZIP, value);
                SelectionPredicate<String> p = (val) -> cityNames.stream().anyMatch(val::startsWith);
                return this.addCustomPredicate(p);
            }
            return this;
        }

        // TODO:  Check handling of abbreviations/full name.  Maybe switch to customPredicate.
        public Builder state(USState value) {
            if(value != null) {
                this.state = value;
                //LOGGER.debug(this.state.getLabel());
                this.equalToMap.putIfAbsent(TemplateField.STATE, value.name());
                return this.states(EnumSet.of(value));
            }
            return this;
        }

        public Builder states(Set<USState> values) {
            if (values == null || values.isEmpty()) return this;
            this.states = values;
            this.inEnumMap.putIfAbsent(TemplateField.STATE, values);
            Set<String> stateNames = new HashSet<>();
            for(USState state : values) {
                stateNames.add("$"+state.name()+"$");
            }
            SelectionPredicate<String> p = (val) -> stateNames.stream().anyMatch(val::contains);
            return this.addCustomPredicate(p);
        }

        // TODO: Is also setting zipCodes necessary?
        public Builder zipCode(String value) {
            if (value == null) return this;
            this.zipCode = value;
            this.equalToMap.putIfAbsent(TemplateField.ZIP_CODE, value);
            return this.zipCodes(Set.of(value));
        }

        /***
         * NOTE: Will overwrite existing zipCodes set. (Does not append)
         * @param values
         * @return Builder
         */
        public Builder zipCodes(Set<String> values) {
            if (values == null || values.isEmpty()) return this;

            this.zipCodes = values;
            this.inMap.putIfAbsent(TemplateField.ZIP_CODE, values);

            // TODO:  Also filter by state?
            Set<String> zipCodeValues = new HashSet<>();
            for(String zipCode : values) {
                zipCodeValues.add("$"+zipCode);
            }
            SelectionPredicate<String> p = (val) -> zipCodeValues.stream().anyMatch(val::contains);
            return this.addCustomPredicate(p);
        }

        public Builder region(USRegion value) {
            if(value != null) {
                this.region = value;
                this.equalToMap.putIfAbsent(TemplateField.REGION, value.getRegionName());
            }
            return this;
        }

        // TODO:  Keep 2 regions options? inMap or predicate for enums?
        //public Builder regions(Set<String> values) {
        //    if (values == null || values.isEmpty()) return this;
        //    this.inMap.putIfAbsent(TemplateField.REGION, values);
        //    return this;
        //}

        public Builder regions(EnumSet<USRegion> values) {
            if (values == null || values.isEmpty()) return this;

            this.inEnumMap.putIfAbsent(TemplateField.REGION, values);
            Set<String> regionNames = values.stream()
                    .map(region -> region.name().toLowerCase())
                    .collect(Collectors.toSet());
            //SelectionPredicate<String> p = (val) -> regionNames.stream().anyMatch(val::equalsIgnoreCase);
            SelectionPredicate<String> p = (val) -> val != null && regionNames.contains(val.toLowerCase());
            return this.addCustomPredicate(p);
        }

        public Builder ethnicity(Ethnicity value) {
            if(value != null) {
                this.ethnicity = value;
                this.equalToMap.putIfAbsent(TemplateField.ETHNICITY, value.getPlaceholder());
            }
            return this;
        }

        public Builder ethnicities(Set<Ethnicity> values) {
            if (values == null || values.isEmpty()) return this;

            this.inEnumMap.putIfAbsent(TemplateField.ETHNICITY, values);
            Set<String> ethnicityNames = values.stream()
                    .map(e -> e.name().toLowerCase())
                    .collect(Collectors.toSet());
            //SelectionPredicate<String> p = (val) -> ethnicityNames.stream().anyMatch(val::equalsIgnoreCase);
            SelectionPredicate<String> p = (val) -> val != null && ethnicityNames.contains(val.toLowerCase());
            return this.addCustomPredicate(p);
        }

        public Builder domainType(DomainType value) {
            if(value != null) {
                this.domainType = value;
                this.equalToMap.putIfAbsent(TemplateField.DOMAIN_TYPE, value.getPlaceholder());
            }
            return this;
        }

        public Builder domain(String value) {
            if(value != null) {
                this.domain = StringUtils.deleteWhitespace(value);
                this.equalToMap.putIfAbsent(TemplateField.DOMAIN, this.domain);
            }
            return this;
        }

        public Builder domains(Set<String> values) {
            if(values != null && !values.isEmpty()) {
                this.inMap.putIfAbsent(TemplateField.DOMAIN, values);
            }
            return this;
        }

        public Builder usernameType(UsernameType value) {
            if(value != null) {
                this.usernameType = value;
                this.equalToMap.putIfAbsent(TemplateField.USERNAME_TYPE, value.getPlaceholder());
            }
            return this;
        }

        public Builder username(String value) {
            if(value != null && !value.isBlank()) {
                // TODO:  Format/validate.  i.e. trim, remove spaces
                this.username = StringUtils.deleteWhitespace(value);
                this.equalToMap.putIfAbsent(TemplateField.USERNAME, this.username);
            }
            return this;
        }

        public Builder phoneNumberType(PhoneNumberType value) {
            if(value == null) return this;
            this.phoneNumberType = value;
            this.inEnumMap.putIfAbsent(TemplateField.PHONE_NUMBER_TYPE, EnumSet.of(value));
            return this;
        }

        public Builder areaCode(String value) {
            if(value != null && !value.isBlank()) {
                this.areaCode = StringUtils.deleteWhitespace(value);
                this.equalToMap.putIfAbsent(TemplateField.AREA_CODE, this.areaCode);
            }
            return this;
        }

        public Builder startsWith(String value, TemplateField field) {
            if(value != null && !value.isBlank()) {
                this.startsWithMap.putIfAbsent(field, value);
            }
            return this;
        }

        public Builder endsWith(String value, TemplateField field) {
            if(value != null) {
                this.endsWithMap.putIfAbsent(field, value);
            }
            return this;
        }

        public Builder contains(String value, TemplateField field) {
            if(value != null) {
                this.containsMap.putIfAbsent(field, value);
            }
            return this;
        }

        protected Builder equalTo(String value, TemplateField field) {
            // TODO:  Check
            if(value != null) {
                this.equalToMap.putIfAbsent(field, value);
            }
            return this;
        }

        public Builder in(Set<String> values, TemplateField field) {
            if(values != null) {
                this.inMap.putIfAbsent(field, values);
            }
            return this;
        }

        // TODO: FilterOperator
        public Builder addFilter(String value, TemplateField field, String filterOperator) {
            // All params need to be present.
            if(value == null) {
                return this;
            }

            switch(filterOperator) {
                case "startswith":
                    return this.startsWith(value, field);
                case "endswith":
                    return this.endsWith(value, field);
                case "contains":
                    return this.contains(value, field);
                case "in":
                    return this.in(Set.of(value), field);
                case "eq":
                    return this.equalTo(value, field);
                default:
                    // TODO:  Do nothing?
                    break;
            }
            return this;
        }

        public <T> Builder addFilter(Set<T> values, TemplateField field, String filterOperator) {
            if (values == null || values.isEmpty()) {
                //LOGGER.debug("Set is null or empty. Cannot accurately determine element types.");
                return this;
            }

            // only add a set filter for fields with EnumSet
            if(!filterOperator.equalsIgnoreCase("in") || !filterOperator.equalsIgnoreCase("eq")) {
                return this;
            }

            Class<?> targetType = field.targetType();
            boolean isEnumType = values instanceof EnumSet 
                    && (EnumSet.class.equals(targetType) || Enum.class.isAssignableFrom(targetType));

            // TODO:  Maybe..
            //T firstElement = values.iterator().next();
            //boolean isSetOfEnums = firstElement instanceof Enum;

            if (isEnumType) {
                // 
                if (values.size() == 1) {
                    // TODO:
                    this.inEnumMap.putIfAbsent(field, values);
                } else {
                    //
                    this.inEnumMap.putIfAbsent(field, values);
                }
                return this;
            }

            if(filterOperator.equalsIgnoreCase("eq") || values.size() == 1) {
                T firstElement = values.iterator().next();
                if (String.class.equals(targetType)) {
                    this.equalToMap.putIfAbsent(field, (String)firstElement);
                    // TODO:  use builder.<field>() instead
                } else {
                    LOGGER.debug("TODO {}", firstElement);
                }
                return this;
            }

            if(filterOperator.equalsIgnoreCase("in")) {
                if (String.class.equals(targetType)) {
                    this.inMap.putIfAbsent(field, (Set<String>) values);
                } else {
                    // TODO:  I don't think this should happen...
                    Set<String> strSet = values.stream().map(v -> v == null ? null : v.toString())
                            .collect(Collectors.toSet());
                    this.inMap.putIfAbsent(field, strSet);
                    LOGGER.debug("TODO {}", values);
                }
            }

            return this;
        }

        private Builder customPredicate(SelectionPredicate<String> predicate) {
            if(predicate == null) return this;

            this.customPredicate = predicate;
            return this.addCustomPredicate(predicate);
        }

        private Builder addCustomPredicate(SelectionPredicate<String> predicate) {
            this.customPredicates.add(predicate);
            return this;
        }

        protected Builder cityStateZip(CityStateZip value) {
            if(value == null) return this;

            return this.city(value.city())
                    .state(USState.fromText(value.state()))
                    .zipCode(value.zip());
        }

        public SelectionFilter build() {
            return new SelectionFilter(
                    Optional.ofNullable(firstName),
                    Optional.ofNullable(middleName),
                    Optional.ofNullable(lastName),
                    Optional.ofNullable(nickName),

                    Optional.ofNullable(gender),
                    Optional.ofNullable(genders),

                    Optional.ofNullable(birthday),

                    Optional.ofNullable(generation),
                    Optional.ofNullable(generations),

                    Optional.ofNullable(minYear),
                    Optional.ofNullable(maxYear),

                    //Optional.ofNullable(postalAddress),
                    Optional.ofNullable(addressCategory),
                    Optional.ofNullable(unitType),
                    Optional.ofNullable(streetName),
                    Optional.ofNullable(streetSuffix),

                    Optional.ofNullable(address2),

                    Optional.ofNullable(city),

                    // TODO: State take precedence over states
                    Optional.ofNullable(state),
                    Optional.ofNullable(states),

                    Optional.ofNullable(zipCode),
                    Optional.ofNullable(zipCodes),
                    Optional.ofNullable(region),
                    Optional.ofNullable(ethnicity),
                    Optional.ofNullable(domainType),
                    Optional.ofNullable(domain),
                    Optional.ofNullable(usernameType),
                    Optional.ofNullable(username),

                    Optional.ofNullable(phoneNumberType),
                    Optional.ofNullable(areaCode),

                    Optional.ofNullable(customPredicate),
                    Optional.ofNullable(customPredicates),

                    startsWithMap,
                    endsWithMap,
                    equalToMap,
                    containsMap,
                    inMap,
                    inEnumMap
            );
        };

        @Override
        public String toString() {
            return this.build().toString();
        }
    }

    @Override
    public String toString() {
        final String FIELD_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        if(!customPredicate.isEmpty()) {
            sb.append("customPredicate=" + "TRUE" + FIELD_DELIM);
        }
        if(!customPredicates.isEmpty()) {
            sb.append("customPredicates=" + "TRUE" + FIELD_DELIM);
        }
        if(startsWithMap != null && !startsWithMap.isEmpty()) {
            sb.append("startsWithMap.size()=" + this.startsWithMap.size() + FIELD_DELIM);
        }
        if(endsWithMap != null && !endsWithMap.isEmpty()) {
            sb.append("endsWithMap.size()=" + this.endsWithMap.size() + FIELD_DELIM);
        }
        if(containsMap != null && !containsMap.isEmpty()) {
            sb.append("containsMap.size()=" + this.containsMap.size() + FIELD_DELIM);
        }
        if(equalToMap != null && !equalToMap.isEmpty()) {
            sb.append("equalToMap.size()=" + this.equalToMap.size() + FIELD_DELIM);
        }
        if(inMap != null && !inMap.isEmpty()) {
            sb.append("inMap.size()=" + this.inMap.size() + FIELD_DELIM);
        }
        if(inEnumMap != null && !inEnumMap.isEmpty()) {
            sb.append("inEnumMap.size()=" + this.inEnumMap.size() + FIELD_DELIM);
        }
        if(!firstName.isEmpty()) sb.append("firstName=" + this.firstName.get()+ FIELD_DELIM);
        if(!middleName.isEmpty()) sb.append("middleName=" + this.middleName.get()+ FIELD_DELIM);
        if(!lastName.isEmpty()) sb.append("lastName=" + this.lastName.get()+ FIELD_DELIM);
        if(!nickName.isEmpty()) sb.append("nickName=" + this.nickName.get()+ FIELD_DELIM);
        if(!gender.isEmpty()) sb.append("gender=" + this.gender.get().name() + FIELD_DELIM);
        if(!genders.isEmpty()) {
            //sb.append("genders=" + Arrays.toString(Stream.of(genders.get()).map(GenderIdentity::name).toArray(String[]::new)) + FIELD_DELIM);
            sb.append("genders=" + genders.get().stream().map(GenderIdentity::name).collect(Collectors.joining()) + FIELD_DELIM);
        }
        //if(!postalAddress.isEmpty()) {
        //    sb.append("postalAddress=" + postalAddress.toString()) + FIELD_DELIM);
        //}
        if(!streetName.isEmpty()) sb.append("streetName=" + this.streetName.get()+ FIELD_DELIM);
        if(!streetSuffix.isEmpty()) sb.append("streetSuffix=" + this.streetSuffix.get()+ FIELD_DELIM);
        if(!address2.isEmpty()) sb.append("address2=" + this.address2.get()+ FIELD_DELIM);

        if(!city.isEmpty()) sb.append("city=" + this.city.get()+ FIELD_DELIM);

        if(!state.isEmpty()) {
            sb.append("state=" + state.get().name() + FIELD_DELIM);
        }
        if(!states.isEmpty()) {
            sb.append("states=" + String.join("$", USState.names(states.get())) + FIELD_DELIM);
        }

        if(!zipCode.isEmpty()) {
            sb.append("zipCode=" + zipCode.get() + FIELD_DELIM);
        }
        if(!zipCodes.isEmpty()) {
            sb.append("zipCodes=" + String.join("$", zipCodes.get()) + FIELD_DELIM);
        }

        if(!birthday.isEmpty()) sb.append("birthday=" + this.birthday.get().toString() + FIELD_DELIM);

        if(!generation.isEmpty()) sb.append("generation=" + this.generation.get().toString() + FIELD_DELIM);
        if(!generations.isEmpty()) sb.append("generations=" + this.generations.get().toString() + FIELD_DELIM);

        if(!ethnicity.isEmpty()) sb.append("ethnicity=" + this.ethnicity.get().toString() + FIELD_DELIM);

        if(!areaCode.isEmpty()) sb.append("areaCode=" + this.areaCode.get()+ FIELD_DELIM);

        if(!usernameType.isEmpty()) sb.append("usernameType=" + this.usernameType.get().toString() + FIELD_DELIM);
        if(!username.isEmpty()) sb.append("username=" + this.username.get()+ FIELD_DELIM);

        if(!domainType.isEmpty()) sb.append("domainType=" + this.domainType.get().toString() + FIELD_DELIM);
        if(!domain.isEmpty()) sb.append("domain=" + this.domain.get().toString() + FIELD_DELIM);
        return sb.toString();
    }

}
