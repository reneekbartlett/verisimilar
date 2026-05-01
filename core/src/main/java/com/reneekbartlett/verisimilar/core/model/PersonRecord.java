package com.reneekbartlett.verisimilar.core.model;

import java.time.LocalDate;

public record PersonRecord(
        FullName fullName,
        GenderIdentity gender,
        LocalDate birthday,
        PostalAddress postalAddress,
        EmailAddressRecord emailAddress,
        PhoneNumber phoneNumber
){

    public PersonRecord(FullName fullName) {
        this(fullName, null, null, null, null, null);
    }

    public PersonRecord(FullName fullName, GenderIdentity gender, LocalDate birthday) {
        this(fullName, gender, birthday, null, null, null);
    }

    public PersonRecord(FullName fullName, LocalDate birthday) {
        this(fullName, null, birthday, null, null, null);
    }

    public PersonRecord(FullName fullName, LocalDate birthday, PostalAddress postalAddress) {
        this(fullName, null, birthday, postalAddress, null, null);
    }

    public String firstName() {
        return this.fullName.firstName();
    }

    public String middleName() {
        return this.fullName.middleName();
    }

    public String lastName() {
        return this.fullName.lastName();
    }

    public String address1() {
        return this.postalAddress.address1();
    }

    public String address2() {
        return this.postalAddress.address2();
    }

    public String city() {
        return this.postalAddress.city();
    }

    public String state() {
        return this.postalAddress.state();
    }

    public String zip() {
        return this.postalAddress.zip();
    }

    public static PersonRecord empty() {
        return new PersonRecord(
                FullName.empty(),
                GenderIdentity.GENDER_UNSPECIFIED,
                LocalDate.now(),
                PostalAddress.empty(),
                EmailAddressRecord.empty(), PhoneNumber.empty()
        );
    }

    public static PersonRecord placeholder() {
        return new PersonRecord(
                FullName.placeholder(),
                GenderIdentity.GENDER_UNSPECIFIED,
                LocalDate.now(),
                PostalAddress.placeholder(),
                EmailAddressRecord.placeholder(),
                PhoneNumber.placeholder()
        );
    }

    @Override
    public String toString() {
        final String FIELD_DELIM = " ";
        return new StringBuilder()
                .append(this.fullName.toString()).append(FIELD_DELIM)
                .append(this.postalAddress.toString()).append(FIELD_DELIM)
                .append(this.birthday).append(FIELD_DELIM)
                .append(this.phoneNumber).append(FIELD_DELIM)
                .append(this.emailAddress).append(FIELD_DELIM)
                .append(this.gender)
                .toString();
    }
}
