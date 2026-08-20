package com.reneekbartlett.verisimilar.core.selector.filter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.model.AddressCategory;
import com.reneekbartlett.verisimilar.core.model.AddressLineOne;
import com.reneekbartlett.verisimilar.core.model.AddressLineTwo;
import com.reneekbartlett.verisimilar.core.model.CityStateZip;
import com.reneekbartlett.verisimilar.core.model.DomainRecord;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.Ethnicity;
import com.reneekbartlett.verisimilar.core.model.FullName;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.Generation;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.PhoneNumberType;
import com.reneekbartlett.verisimilar.core.model.PostalAddress;
import com.reneekbartlett.verisimilar.core.model.StreetAddress;
import com.reneekbartlett.verisimilar.core.model.StreetSuffix;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.model.USRegion;
import com.reneekbartlett.verisimilar.core.model.USState;
import com.reneekbartlett.verisimilar.core.model.UnitType;
import com.reneekbartlett.verisimilar.core.model.UsernameType;
import com.reneekbartlett.verisimilar.core.selector.filter.EntryFilter.DescribedPredicate;

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
        Optional<String> unitNumber,

        Optional<AddressLineOne> address1,
        Optional<String> streetId,
        Optional<String> streetName,
        Optional<String> streetSuffix,

        Optional<AddressLineTwo> address2,

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
        Optional<String> phoneNumber,

        Optional<Set<SelectionPredicate<String>>> customPredicates,

        Map<TemplateField, Set<DescribedPredicate<String>>> customPredicateMap,

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

//    public Optional<AddressLineOne> addressLineOne(){
//        //AddressLineOne (String streetId, String streetName, String streetSuffix
//        String streetId = streetId().orElse(null);
//        String streetName = streetName().orElse(null);
//        String streetSuffix = streetSuffix().orElse(null);
//        AddressCategory addressCategory = addressCategory().orElse(null);
//        return Optional.of(new AddressLineOne(streetId, streetName, streetSuffix, addressCategory));
//    }
//
//    public Optional<AddressLineTwo> addressLineTwo(){
//        //AddressLineTwo (String unitNumber, String unitXtra, UnitType unitType, AddressCategory addressCategory)
//        String unitNumber = unitNumber().orElse(null);
//        //String unitXtra = unitXtra().orElse(null);
//        UnitType unitType = unitType().orElse(null);
//        AddressCategory addressCategory = addressCategory().orElse(null);
//        return Optional.of(new AddressLineTwo(unitNumber, null, unitType, addressCategory));
//    }

    public Optional<CityStateZip> cityStateZip(){
        String city = city().orElse(null);
        USState state = state().orElse(null);
        String zipCode = zipCode().orElse(null);

        StringBuilder sbId = new StringBuilder(0);
        if(city != null) sbId.append(city);
        if(state != null) sbId.append("$" + state().get().getLabel() + "$");
        if(zipCode != null) sbId.append(zipCode);

        return Optional.of(new CityStateZip(city, state, zipCode, sbId.toString()));
    }

    public Optional<StreetAddress> streetAddress(){
        AddressCategory addressCategory = addressCategory().orElse(null);
        AddressLineOne address1 = address1().orElse(null);
        AddressLineTwo address2 = address2().orElse(null);
        return Optional.of(new StreetAddress(address1, address2, addressCategory));
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

        customPredicates = customPredicates == null ? Optional.empty() : customPredicates;
    }

    /***
     * Make a copy of SelectionFilter
     * @param filter
     * @return
     */
    public static Builder toBuilder(SelectionFilter filter) {
        if(filter == null) {
            return new Builder();
        }
        return new Builder(filter);
    }

    // TODO: clone?
    //private Builder toBuilder() { return new Builder(this); }

    public PersonRecord getPersonRecord() {
        FullName fullName = new FullName(firstName.orElse(""), middleName.orElse(""), lastName.orElse(""));
        GenderIdentity genderIdentity = gender.orElse(GenderIdentity.GENDER_UNSPECIFIED);
        return new PersonRecord(
                fullName,
                genderIdentity,
                birthday.orElse(null)
        );
    }

    public boolean isEmpty() {
        return customPredicates.isEmpty()
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

                Optional.empty(), // AddressCategory
                Optional.empty(), // UnitType
                Optional.empty(), // UnitNumber

                Optional.empty(), // address1
                Optional.empty(), // streetId
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
                Optional.empty(), // phoneNumber

                Optional.empty(), // customPredicates
                new ConcurrentHashMap<>(), //customPredicateMap

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
        private String unitNumber;

        @SuppressWarnings("unused")
        private StreetAddress streetAddress;

        private AddressLineOne address1;

        private String streetId;
        private String streetName;
        private String streetSuffix;

        private AddressLineTwo address2;

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
        private String phoneNumber;

        protected final Set<SelectionPredicate<String>> customPredicates = ConcurrentHashMap.newKeySet();

        protected final Map<TemplateField, String> startsWithMap = new ConcurrentHashMap<>();
        protected final Map<TemplateField, String> endsWithMap = new ConcurrentHashMap<>();

        protected final Map<TemplateField, String> containsMap = new ConcurrentHashMap<>();

        protected final Map<TemplateField, String> equalToMap = new ConcurrentHashMap<>();
        protected final Map<TemplateField, Set<String>> inMap = new ConcurrentHashMap<>();
        protected final Map<TemplateField, Set<?>> inEnumMap = new ConcurrentHashMap<>();

        protected final Map<TemplateField, Set<DescribedPredicate<String>>> customPredicateMap = new ConcurrentHashMap<>();

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
            this.unitNumber = filter.unitNumber.orElse(null);

            this.streetAddress = filter.streetAddress().orElse(null);

            this.address1 = filter.address1.orElse(null);

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

            filter.customPredicateMap().forEach((k,v) -> this.customPredicateMap.putIfAbsent(k, v));

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
                //this.genders(EnumSet.of(value));
            }
            return this;
        }

        public Builder genders(Set<GenderIdentity> values) {
            if (values == null || values.isEmpty()) return this;

            this.genders = values;
            this.inEnumMap.putIfAbsent(TemplateField.GENDER_IDENTITY, values);
            return this;
        }

        public Builder birthday(String value) {
            if(value != null) {
                try {
                    // Call birthday with parsed LocalDate
                    LocalDate parsedDate = LocalDate.parse(value, DATE_FORMATTER);
                    this.birthday(parsedDate);
                } catch (Exception e) {
                    LOGGER.warn("Invalid date format for field {}", TemplateField.BIRTHDAY);
                }
            }
            return this;
        }

        public Builder birthday(LocalDate value) {
            if(value != null) {
                this.birthday = value;
                this.equalToMap.putIfAbsent(TemplateField.BIRTHDAY, value.format(DATE_FORMATTER));
            }
            return this;
        }

        public Builder generation(Generation value) {
            if(value != null) {
                this.generation = value;
                this.equalToMap.putIfAbsent(TemplateField.GENERATION, value.getLabel());
            }
            return this;
        }

        public Builder generations(Set<Generation> values) {
            if(values != null) {
                this.generations = values;
                this.inEnumMap.putIfAbsent(TemplateField.GENERATION, values);
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

        /***
         * Combined field containing AddressLineOne, AddressLineTwo, CityStateZip, AddressCategory
         */
        public Builder postalAddress(PostalAddress value) {
            if(value != null) {
                this.address1(value.addressLineOne());
                this.address2(value.addressLineTwo());
                this.cityStateZip(value.cityStateZip());
                this.addressCategory(value.addressCategory());
            }
            return this;
        }

        public Builder addressCategory(AddressCategory value) {
            if(value == null || value == AddressCategory.EMPTY) return this;
            this.addressCategory = value;
            this.equalToMap.putIfAbsent(TemplateField.ADDRESS_CATEGORY, value.getLabel());
            this.inEnumMap.putIfAbsent(TemplateField.ADDRESS_CATEGORY, EnumSet.of(value));
            return this;
        }

        public Builder unitType(UnitType value) {
            if(value == null || value == UnitType.UNKNOWN) return this;
            this.unitType = value;
            this.equalToMap.putIfAbsent(TemplateField.UNIT_TYPE, value.getLabel());
            this.inEnumMap.putIfAbsent(TemplateField.UNIT_TYPE, EnumSet.of(value));
            return this;
        }

        public Builder unitNumber(String value) {
            if(value == null) return this;
            this.unitNumber = value;
            this.equalToMap.putIfAbsent(TemplateField.UNIT_NUMBER, value);
            return this;
        }

        public Builder streetId(String value) {
            if(value == null) return this;
            this.streetId = value;
            this.equalToMap.putIfAbsent(TemplateField.STREET_ID, value);
            return this;
        }

        public Builder streetName(String value) {
            if(value == null) return this;
            this.streetName = value;
            this.equalToMap.putIfAbsent(TemplateField.STREET_NAME, value);
            return this;
        }

        // TODO: Use enum?
        public Builder streetSuffix(String value) {
            if(value == null) return this;
            this.streetSuffix = value;
            this.equalToMap.putIfAbsent(TemplateField.STREET_SUFFIX, this.streetSuffix);
            this.inEnumMap.putIfAbsent(TemplateField.STREET_SUFFIX, EnumSet.of(StreetSuffix.fromLabel(value)));
            return this;
        }

        public Builder streetSuffix(StreetSuffix value) {
            if(value == null) return this;
            this.streetSuffix = value.getLabel();
            this.equalToMap.putIfAbsent(TemplateField.STREET_SUFFIX, value.getLabel());
            this.inEnumMap.putIfAbsent(TemplateField.STREET_SUFFIX, EnumSet.of(value));
            return this;
        }

        /***
         * Combined Field
         * @param value
         * @return
         */
        public Builder address1(AddressLineOne value) {
            if(value != null) {
                this.address1 = value;
                this.equalToMap.putIfAbsent(TemplateField.ADDRESS1, value.address1());

                this.streetId(value.streetId());
                this.streetName(value.streetName());
                this.streetSuffix(value.streetSuffix());

                this.addressCategory(value.addressCategory());
            }
            return this;
        }

        public Builder address2(AddressLineTwo value) {
            if(value != null) {
                this.address2 = value;
                this.equalToMap.putIfAbsent(TemplateField.ADDRESS2, value.toString());

                this.unitType(value.unitType());
                this.unitNumber(value.unitNumber());
                //value.unitXtra()
                this.addressCategory(value.addressCategory());
            }
            return this;
        }

        /***
         * Since the City value comes from CityStateZip Selection Engine, use custom predicates for matching
         * @param value
         * @return
         */
        public Builder city(String value) {
            if(value != null) {
                this.city = StringUtils.trim(value);
                this.equalToMap.putIfAbsent(TemplateField.CITY, this.city);
            }
            return this;
        }

        // TODO:  Check handling of abbreviations/full name.  Maybe switch to customPredicate.
        public Builder state(USState value) {
            if(value != null) {
                this.state = value;
                this.equalToMap.putIfAbsent(TemplateField.STATE, value.getLabel());
            }
            return this;
        }

        /***
         * Since the USState value comes from CityStateZip Selection Engine, use custom predicates for matching
         * @param values
         * @return
         */
        public Builder states(Set<USState> values) {
            if (values == null || values.isEmpty()) return this;
            this.states = values;
            this.inEnumMap.putIfAbsent(TemplateField.STATE, values);
            this.inMap.putIfAbsent(TemplateField.STATE, values.stream().map(USState::getLabel).collect(Collectors.toSet()));
            return this;
        }

        public Builder zipCode(String value) {
            if (value == null) return this;
            this.zipCode = StringUtils.trim(value);
            this.equalToMap.putIfAbsent(TemplateField.ZIP_CODE, this.zipCode);
            return this;
        }

        /***
         * Since the ZipCode value comes from CityStateZip Selection Engine, use custom predicates for matching
         * @param values
         * @return Builder
         */
        public Builder zipCodes(Set<String> values) {
            if (values == null || values.isEmpty()) return this;
            this.zipCodes = values;
            this.inMap.putIfAbsent(TemplateField.ZIP_CODE, values);
            return this;
        }

        public Builder region(USRegion value) {
            if(value != null) {
                this.region = value;
                this.equalToMap.putIfAbsent(TemplateField.REGION, value.getRegionName());
                //this.regions(EnumSet.of(value));
            }
            return this;
        }

        public Builder regions(EnumSet<USRegion> values) {
            if (values == null || values.isEmpty()) return this;
            // TODO:  Add regions variable?
            this.inEnumMap.putIfAbsent(TemplateField.REGION, values);
            this.inMap.putIfAbsent(TemplateField.REGION, values.stream().map(USRegion::getLabel).collect(Collectors.toSet()));
            return this;
        }

        public Builder ethnicity(Ethnicity value) {
            if(value != null) {
                this.ethnicity = value;
                this.equalToMap.putIfAbsent(TemplateField.ETHNICITY, value.getPlaceholder());
                //this.ethnicities(EnumSet.of(value));
            }
            return this;
        }

        public Builder ethnicities(Set<Ethnicity> values) {
            if (values == null || values.isEmpty()) return this;
            this.inEnumMap.putIfAbsent(TemplateField.ETHNICITY, values);
            this.inMap.putIfAbsent(TemplateField.ETHNICITY, values.stream().map(Ethnicity::getLabel).collect(Collectors.toSet()));
            return this;
        }

        public Builder domainType(DomainType value) {
            if(value != null) {
                this.domainType = value;
                this.equalToMap.putIfAbsent(TemplateField.DOMAIN_TYPE, value.getPlaceholder());
                // TODO:  Add domainTypes?
                //this.inEnumMap.putIfAbsent(TemplateField.DOMAIN_TYPE, EnumSet.of(value));
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
                this.equalToMap.putIfAbsent(TemplateField.USERNAME_TYPE, value.getLabel());
                // TODO:  Add usernameTypes variable?
                //this.inEnumMap.putIfAbsent(TemplateField.USERNAME_TYPE, EnumSet.of(value));
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
            this.equalToMap.putIfAbsent(TemplateField.PHONE_NUMBER_TYPE, value.getLabel());
            // TODO:  Add phoneNumberTypes?
            //this.inEnumMap.putIfAbsent(TemplateField.PHONE_NUMBER_TYPE, EnumSet.of(value));
            return this;
        }

        public Builder areaCode(String value) {
            if(value != null && !value.isBlank()) {
                this.areaCode = StringUtils.deleteWhitespace(value);
                this.equalToMap.putIfAbsent(TemplateField.AREA_CODE, this.areaCode);
            }
            return this;
        }

        /***
         * StreetAddress(AddressLineOne addressLineOne, AddressLineTwo addressLineTwo, AddressCategory addressCategory)
         * @param value
         * @return
         */
        public Builder streetAddress(StreetAddress value) {
            if(value == null) return this;
            this.streetAddress = value;

            return this.address1(value.addressLineOne())
                .address2(value.addressLineTwo())
                .addressCategory(value.addressCategory());
        }

        /***
         * Combined field with City (String), State (USState), ZipCode (String)
         * @param value
         * @return
         */
        protected Builder cityStateZip(CityStateZip value) {
            if(value == null) return this;
            return this.city(value.city())
                    .state(USState.fromText(value.state()))
                    .zipCode(value.zip());
        }

        /**
         * To access outside of SelectionFilter, use addFilter
         */
        protected Builder startsWith(String value, TemplateField field) {
            if(value != null && !value.isBlank()) {
                this.startsWithMap.putIfAbsent(field, value);
                if(field.equals(TemplateField.CITY_STATE_ZIP)) {
                    SelectionPredicate<String> c = EntryFilter.startsWithSubstring(value, true);
                    this.addCustomPredicate(c, TemplateField.CITY_STATE_ZIP, "starts with " + value);
                }
            }
            return this;
        }

        /**
         * To access outside of SelectionFilter, use addFilter
         */
        protected Builder endsWith(String value, TemplateField field) {
            if(value != null) {
                this.endsWithMap.putIfAbsent(field, value);
            }
            return this;
        }

        /**
         * To access outside of SelectionFilter, use addFilter
         */
        protected Builder contains(String value, TemplateField field) {
            if(value != null) {
                this.containsMap.putIfAbsent(field, value);
            }
            return this;
        }

        /**
         * To access outside of SelectionFilter, use addFilter
         */
        protected Builder equalTo(String value, TemplateField field) {
            if(value != null) {
                //this.equalToMap.putIfAbsent(field, value);
                SelectionFieldMapper.LOOKUP.get(field).applySingle(this, value);
            }
            return this;
        }

        public Builder in(Set<?> values, TemplateField field) {
            if(values != null && !values.isEmpty()) {
                //this.inMap.putIfAbsent(field, values);
                SelectionFieldMapper.LOOKUP.get(field).applyMulti(this, values);
            }
            return this;
        }

        public <E> Builder inEnum(Set<?> values, TemplateField field) {
            if(values != null && !values.isEmpty()) {
                this.inEnumMap.putIfAbsent(field, values);
                SelectionFieldMapper.LOOKUP.get(field).applyMulti(this, values);
            }
            return this;
        }

        // TODO: FilterOperator
        public Builder addFilter(String value, TemplateField field, String filterOperator) {
            if(value == null || field == null || filterOperator == null) {
                return this;
            }

            switch(filterOperator) {
                case "startswith":
                    // custom handling for CITY/STATE/ZIP dataset
                    if(field.equals(TemplateField.CITY)) {
                        this.addFilter(value, TemplateField.CITY_STATE_ZIP, "startswith");
                    }
                    if(field.equals(TemplateField.STATE)) {
                        this.addFilter("$" + value, TemplateField.CITY_STATE_ZIP, "contains");
                    }
                    if(field.equals(TemplateField.ZIP_CODE)) {
                        this.addFilter("$" + value, TemplateField.CITY_STATE_ZIP, "contains");
                    }

                    // add to startsWith map
                    return this.startsWith(value, field);
                case "endswith":
                    // custom handling for CITY/STATE/ZIP dataset
                    if(field.equals(TemplateField.CITY)) {
                        this.addFilter(value + "$", TemplateField.CITY_STATE_ZIP, "contains");
                    }
                    if(field.equals(TemplateField.STATE)) {
                        this.addFilter(value + "$", TemplateField.CITY_STATE_ZIP, "contains");
                    }
                    if(field.equals(TemplateField.ZIP_CODE)) {
                        this.addFilter(value, TemplateField.CITY_STATE_ZIP, "endswith");
                    }

                    // add to endsWith map
                    return this.endsWith(value, field);
                case "contains":
                    // custom handling for CITY/STATE/ZIP dataset
                    if(field.equals(TemplateField.CITY) || field.equals(TemplateField.STATE) || field.equals(TemplateField.ZIP_CODE)) {
                        this.addFilter(value, TemplateField.CITY_STATE_ZIP, "contains");
                    }

                    // add to contains map
                    return this.contains(value, field);
                case "eq":
                    return this.equalTo(value, field);
                case "in":
                    // Pass to addFilter(Set<T>...)
                    return this.addFilter(Set.of(value), field, filterOperator);
                default:
                    // TODO:  Do nothing?
                    LOGGER.warn("addFilter - unhandled operator.");
                    break;
            }
            return this;
        }

        /***
         * IN and EQ only match full value so EntryFilters will get applied at the field level.
         * 
         * @param <T>
         * @param values
         * @param field
         * @param filterOperator
         * @return
         */
        public <T> Builder addFilter(Set<T> values, TemplateField field, String filterOperator) {
            if (values == null || values.isEmpty()) {
                //LOGGER.debug("Set is null or empty. Cannot accurately determine element types.");
                return this;
            }

            // only add a set filter for fields with EnumSet
            if(!filterOperator.equalsIgnoreCase("in") && !filterOperator.equalsIgnoreCase("eq")) {
                return this;
            }

            T firstElement = values.iterator().next();

            switch(filterOperator) {
                case "in":
                    SelectionFieldMapper.LOOKUP.get(field).applyMulti(this, values);
                    // TODO:  Remove.. adding for testing
                    Set<String> strSet = TemplateField.isEnumField(field) ? SelectionFieldMapper.toEnumNameSet(values) : 
                        SelectionFieldMapper.castStringSet(values);
                    LOGGER.debug("addFilter - field {} in {}", field.getLabel(), strSet);
                    break;
                case "eq":
                    SelectionFieldMapper eqFieldMapper = SelectionFieldMapper.LOOKUP.get(field);
                    eqFieldMapper.applySingle(this, firstElement);
                    Set<String> eqStrSet = TemplateField.isEnumField(field) ? SelectionFieldMapper.toEnumNameSet(Set.of(firstElement)) : 
                        SelectionFieldMapper.castStringSet(Set.of(firstElement));
                    LOGGER.debug("addFilter - {} eq {}?", field.getLabel(), eqStrSet);
                    break;
                default:
                    LOGGER.warn("addFilter - unhandled operator.");
                    break;
            }
            return this;
        }

        // TODO: Not used.
        protected Builder addCustomPredicate(SelectionPredicate<String> predicate, TemplateField field, String desc) {
            if(predicate == null) return this;
            this.customPredicates.add(predicate);

            DescribedPredicate<String> describedPredicate = new DescribedPredicate<String>(predicate, field.getLabel()+" "+desc);
            //LOGGER.debug("describedPredicate[text='{}']", describedPredicate.text());
            this.customPredicateMap.merge(field, new HashSet<>(Set.of(describedPredicate)), (oldSet, newSet) -> {
                oldSet.addAll(newSet);
                return oldSet;
            });

            LOGGER.debug("{}", customPredicateMap.keySet());

            return this;
        }

        public SelectionFilter build() {
            // TODO: Check if fields have individual values set
            //Set<TemplateField> equalToKeys = equalToMap.keySet();
            //Set<TemplateField> inKeys = inMap.keySet();

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
                    Optional.ofNullable(unitNumber),
                    Optional.ofNullable(address1),
                    Optional.ofNullable(streetId),
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
                    Optional.ofNullable(phoneNumber),

                    Optional.ofNullable(customPredicates),

                    customPredicateMap,

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
        //if(!customPredicates.isEmpty()) {
        //    sb.append("customPredicates=" + "TRUE" + FIELD_DELIM);
        //}
        if(customPredicateMap != null && !customPredicateMap.isEmpty()) {
            sb.append("customPredicateMap.size()=" + this.customPredicateMap.size() + FIELD_DELIM);
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

        //
        // 
        //
        if(!firstName.isEmpty()) sb.append("firstName=" + this.firstName.get()+ FIELD_DELIM);
        if(!middleName.isEmpty()) sb.append("middleName=" + this.middleName.get()+ FIELD_DELIM);
        if(!lastName.isEmpty()) sb.append("lastName=" + this.lastName.get()+ FIELD_DELIM);
        if(!nickName.isEmpty()) sb.append("nickName=" + this.nickName.get()+ FIELD_DELIM);

        if(!gender.isEmpty()) sb.append("gender=" + this.gender.get().name() + FIELD_DELIM);
        if(!genders.isEmpty()) {
            //sb.append("genders=" + Arrays.toString(Stream.of(genders.get()).map(GenderIdentity::name).toArray(String[]::new)) + FIELD_DELIM);
            sb.append("genders=" + genders.get().stream().map(GenderIdentity::name).collect(Collectors.joining()) + FIELD_DELIM);
        }

        if(!addressCategory.isEmpty()) sb.append("addressCategory=" + this.addressCategory.get().toString()+ FIELD_DELIM);
        if(!streetId.isEmpty()) sb.append("streetId=" + this.streetId.get()+ FIELD_DELIM);

        if(!streetName.isEmpty()) sb.append("streetName=" + this.streetName.get()+ FIELD_DELIM);
        if(!streetSuffix.isEmpty()) sb.append("streetSuffix=" + this.streetSuffix.get()+ FIELD_DELIM);

        if(!address1.isEmpty()) sb.append("address1=" + this.address1.get().toString()+ FIELD_DELIM);
        if(!address2.isEmpty()) sb.append("address2=" + this.address2.get().toString()+ FIELD_DELIM);

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

        if(!unitNumber.isEmpty()) {
            sb.append("unitNumber=" + unitNumber.get().toString() + FIELD_DELIM);
        }

        if(!unitType.isEmpty()) {
            sb.append("unitType=" + unitType.get().toString() + FIELD_DELIM);
        }

        if(!birthday.isEmpty()) sb.append("birthday=" + this.birthday.get().toString() + FIELD_DELIM);

        if(!generation.isEmpty()) sb.append("generation=" + this.generation.get().toString() + FIELD_DELIM);
        if(!generations.isEmpty()) sb.append("generations=" + this.generations.get().toString() + FIELD_DELIM);

        if(!ethnicity.isEmpty()) sb.append("ethnicity=" + this.ethnicity.get().toString() + FIELD_DELIM);

        if(!phoneNumberType.isEmpty()) sb.append("phoneNumberType=" + this.phoneNumberType.get().toString()+ FIELD_DELIM);
        if(!areaCode.isEmpty()) sb.append("areaCode=" + this.areaCode.get()+ FIELD_DELIM);

        if(!usernameType.isEmpty()) sb.append("usernameType=" + this.usernameType.get().toString() + FIELD_DELIM);
        if(!username.isEmpty()) sb.append("username=" + this.username.get()+ FIELD_DELIM);

        if(!domainType.isEmpty()) sb.append("domainType=" + this.domainType.get().toString() + FIELD_DELIM);
        if(!domain.isEmpty()) sb.append("domain=" + this.domain.get().toString() + FIELD_DELIM);
        return sb.toString();
    }

}
