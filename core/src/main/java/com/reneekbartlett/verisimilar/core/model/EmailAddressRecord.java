package com.reneekbartlett.verisimilar.core.model;

import ch.qos.logback.core.util.StringUtil;

public record EmailAddressRecord(String username, String domain, String type){

    public EmailAddressRecord(String username, String domain) {
        this(username, domain, null);
    }

    public static EmailAddressRecord empty() {
        return new EmailAddressRecord(null, null, null);
    }

    public static EmailAddressRecord placeholder() {
        return new EmailAddressRecord("MAYOR", "BOSTON.GOV", "GOV");
    }

    public String email() {
        EmailAddressRecord placeholder = EmailAddressRecord.placeholder();
        return new StringBuilder()
                .append(!StringUtil.isNullOrEmpty(username) ? username : placeholder.username)
                .append("@")
                .append(!StringUtil.isNullOrEmpty(domain) ? domain : placeholder.domain)
                .toString();
    }

    @Override
    public String toString() {
        //final String VALUE_DELIM = " ";
        return new StringBuilder()
            .append(this.username).append("@")
            .append(this.domain)//.append(VALUE_DELIM)
            //.append(this.type)
            .toString().toUpperCase();
    }
}
