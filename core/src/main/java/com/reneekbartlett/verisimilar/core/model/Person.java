package com.reneekbartlett.verisimilar.core.model;

import java.time.LocalDate;

public record Person<T1, T2, T3, T4, T5>(
        FullName fullName,
        LocalDate birthday,
        //PostalAddress<String, String, String, String, String> postalAddress,
        PostalAddress postalAddress,
        String phoneNumber,
        String emailAddress
        ){

    public String firstName() {
        return this.fullName.firstName();
    }

    public String lastName() {
        return this.fullName.lastName();
    }

    @Override
    public String toString() {
        final String FIELD_DELIM = "";
        return new StringBuilder()
                .append(this.fullName.toString()).append(FIELD_DELIM)
                .append(this.postalAddress.toString()).append(FIELD_DELIM)
                .append(this.birthday).append(FIELD_DELIM)
                .append(this.phoneNumber).append(FIELD_DELIM)
                .append(this.emailAddress)
                .toString();
    }
}
