package com.reneekbartlett.verisimilar.api.dto;

import java.time.LocalDate;

import com.reneekbartlett.verisimilar.core.model.GenderIdentity;

public class PersonResponseDto {

    private String firstName;
    private String middleName;
    private String lastName;
    private GenderIdentity genderIdentity;
    private LocalDate birthday;
    
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String zip;

    private String phoneNumber;
    private String emailAddress;

    public PersonResponseDto() {
        
    }

    public PersonResponseDto(String firstName, String middleName, String lastName, LocalDate birthday, GenderIdentity genderIdentity
            ,String address1, String address2, String city, String state, String zip, String emailAddress, String phoneNumber) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.genderIdentity = genderIdentity;
        this.addr1 = address1;
        this.addr2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public GenderIdentity getGender() {
        return genderIdentity;
    }

    public String getAddress1() {
        return addr1;
    }

    public String getAddress2() {
        return addr2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
