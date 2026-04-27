package com.reneekbartlett.verisimilar.core.model;

import java.time.LocalDate;
import java.util.Optional;

//import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {

    private final String profileId;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String nickname;
    private final String maidenName;
    private final String suffix; // FullNameSuffix
    private final String prefix; // FullNamePrefix
    private final String altName;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;
    private final Integer age;
    private final Generation generation;
    private final GenderIdentity genderIdentity;
    private final AstrologySign astrologySign;

    private final String emailAddress;
    private final String phoneNumber;

    private final String address1;
    private final String address2;
    private final String city;
    private final USState state;
    private final String zip;
    private final String country;
    private final USRegion region;

    private final Boolean isHeadOfHousehold;
    private final Integer householdSize;
    private final Integer siblingCount;
    private final Integer childrenCount;

    private final Occupation occupation;
    private final EducationLevel educationLevel;
    private final Religion religion;
    private final Ethnicity ethnicity;

    private Profile(Builder b){
        this.profileId = b.profileId;
        this.firstName = b.firstName;
        this.middleName = b.middleName;
        this.lastName = b.lastName;
        this.nickname = b.nickname;
        this.maidenName = b.maidenName;
        this.suffix = b.suffix;
        this.prefix = b.prefix;
        this.altName = b.altName;

        this.birthday = b.birthdate;
        this.age = b.age;
        this.generation = b.generation;
        this.genderIdentity = b.genderIdentity;
        this.astrologySign = b.astrologySign;

        this.emailAddress = b.emailAddress;
        this.phoneNumber = b.phoneNumber;

        this.address1 = b.address1;
        this.address2 = b.address2;
        this.city = b.city;
        this.state = b.state;
        this.zip = b.zip;
        this.country = b.country;
        this.region = b.region;

        this.isHeadOfHousehold = b.isHeadOfHousehold;
        this.householdSize = b.householdSize;
        this.siblingCount = b.siblingCount;
        this.childrenCount = b.childrenCount;

        this.occupation = b.occupation;
        this.educationLevel = b.educationLevel;
        this.religion = b.religion;
        this.ethnicity = b.ethnicity;
    }

    public String getProfileId() { return profileId; }

    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }

    public Optional<String> getNickname() { return Optional.ofNullable(nickname); }
    public Optional<String> getMaidenName() { return Optional.ofNullable(maidenName); }
    public Optional<String> getSuffix() { return Optional.ofNullable(suffix); }
    public Optional<String> getPrefix() { return Optional.ofNullable(prefix); }
    public Optional<String> getAltName() { return Optional.ofNullable(altName); }

    public LocalDate getBirthdate() { return birthday; }
    public Integer getAge() { return age; }
    public Generation getGeneration() { return generation; }
    public GenderIdentity getGenderIdentity() { return genderIdentity; }
    public AstrologySign getAstrologySign() { return astrologySign; }

    public String getEmailAddress() { return emailAddress; }
    public String getPhoneNumber() { return phoneNumber; }

    public String getAddress1() { return address1; }
    public String getAddress2() { return address2; }
    public String getCity() { return city; }
    public USState getState() { return state; }
    public String getZip() { return zip; }
    public String getCountry() { return country; }
    public USRegion getRegion() { return region; }

    public Optional<Boolean> getIsHeadOfHousehold() { return Optional.ofNullable(isHeadOfHousehold); }
    public Optional<Integer> getHouseholdSize() { return Optional.ofNullable(householdSize); }
    public Optional<Integer> getSiblingCount() { return Optional.ofNullable(siblingCount); }
    public Optional<Integer> getChildrenCount() { return Optional.ofNullable(childrenCount); }

    public Optional<Occupation> getOccupation() { return Optional.ofNullable(occupation); }
    public Optional<EducationLevel> getEducationLevel() { return Optional.ofNullable(educationLevel); }

    public Optional<Ethnicity> getEthnicity() { return Optional.ofNullable(ethnicity); }
    public Optional<Religion> getReligion() { return Optional.ofNullable(religion); }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String profileId;
        private String firstName;
        private String middleName;
        private String lastName;
        private String nickname;
        private String maidenName;
        private String suffix;
        private String prefix;
        private String altName;

        private LocalDate birthdate;
        private Integer age;
        private Generation generation;
        private AstrologySign astrologySign;

        private GenderIdentity genderIdentity;

        private String emailAddress;
        private String phoneNumber;

        private String address1;
        private String address2;
        private String city;
        private USState state;
        private String zip;
        private String country;
        private USRegion region;

        private Boolean isHeadOfHousehold;
        private Integer householdSize;
        private Integer siblingCount;
        private Integer childrenCount;

        private Occupation occupation;
        private EducationLevel educationLevel;
        private Religion religion;
        private Ethnicity ethnicity;

        private Builder() {}

        public Builder profileId(String profileId) { this.profileId = profileId; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder middleName(String middleName) { this.middleName = middleName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder nickname(String nickname) { this.nickname = nickname; return this; }
        public Builder maidenName(String maidenName) { this.maidenName = maidenName; return this; }
        public Builder suffix(String suffix) { this.suffix = suffix; return this; }
        public Builder prefix(String prefix) { this.prefix = prefix; return this; }
        public Builder altName(String altName) { this.altName = altName; return this; }

        public Builder birthdate(LocalDate birthdate) { this.birthdate = birthdate; return this; }
        public Builder age(Integer age) { this.age = age; return this; }
        public Builder generation(Generation generation) { this.generation = generation; return this; }
        public Builder genderIdentity(GenderIdentity genderIdentity) { this.genderIdentity = genderIdentity; return this; }
        public Builder astrologySign(AstrologySign astrologySign) { this.astrologySign = astrologySign; return this; }

        public Builder emailAddress(String emailAddress) { this.emailAddress = emailAddress; return this; }
        public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }

        public Builder address1(String address1) { this.address1 = address1; return this; }
        public Builder address2(String address2) { this.address2 = address2; return this; }
        public Builder city(String city) { this.city = city; return this; }
        public Builder state(USState state) { this.state = state; return this; }
        public Builder zip(String zip) { this.zip = zip; return this; }
        public Builder country(String country) { this.country = country; return this; }
        public Builder region(USRegion region) { this.region = region; return this; }

        public Builder isHeadOfHousehold(Boolean isHeadOfHousehold) { this.isHeadOfHousehold = isHeadOfHousehold; return this; }
        public Builder householdSize(Integer householdSize) { this.householdSize = householdSize; return this; }
        public Builder siblingCount(Integer siblingCount) { this.siblingCount = siblingCount; return this; }
        public Builder childrenCount(Integer childrenCount) { this.childrenCount = childrenCount; return this; }

        public Builder occupation(Occupation occupation) { this.occupation = occupation; return this; }
        public Builder educationLevel(EducationLevel educationLevel) { this.educationLevel = educationLevel; return this; }
        public Builder religion(Religion religion) { this.religion = religion; return this; }
        public Builder ethnicity(Ethnicity ethnicity) { this.ethnicity = ethnicity; return this; }

        public Profile build() { return new Profile(this); }
    }
}