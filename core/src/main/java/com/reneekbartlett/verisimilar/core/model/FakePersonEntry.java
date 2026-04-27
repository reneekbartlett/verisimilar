// package com.reneekbartlett.verisimilar.core.model;

// import java.time.LocalDate;

// //public record FakePersonEntry<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>(
// public record FakePersonEntry(
//         String firstName,
//         String middleName,
//         String lastName,
//         LocalDate birthday,
//         String addr1,
//         String addr2,
//         String city,
//         String state,
//         String zip,
//         String phoneNumber,
//         String emailAddress
//         ){
//     @Override
//     public String toString() {
//         String FIELD_DELIM = "\t";
//         return new StringBuilder()
//                 .append(this.firstName.toString()).append(FIELD_DELIM)
//                 .append(this.middleName.toString()).append(FIELD_DELIM)
//                 .append(this.lastName.toString()).append(FIELD_DELIM)
//                 .append(this.birthday).append(FIELD_DELIM)
//                 .append(this.addr1).append(FIELD_DELIM)
//                 .append(this.addr2).append(FIELD_DELIM)
//                 .append(this.city).append(FIELD_DELIM)
//                 .append(this.state).append(FIELD_DELIM)
//                 .append(this.zip).append(FIELD_DELIM)
//                 .append(this.phoneNumber).append(FIELD_DELIM)
//                 .append(this.emailAddress)
//                 .toString();
//     }
// }
