package com.reneekbartlett.verisimilar.core.model;

import ch.qos.logback.core.util.StringUtil;

public record EmailAddressRecord(String username, String domain, DomainType type){

    public EmailAddressRecord(String username, String domain) {
        this(username, domain, null);
    }

    public static EmailAddressRecord empty() {
        return new EmailAddressRecord(null, null, null);
    }

    public static EmailAddressRecord placeholder() {
        return new EmailAddressRecord("MAYOR", "BOSTON.GOV", DomainType.GOV);
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
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        sb.append(this.username).append("@");
        sb.append(this.domain).append(VALUE_DELIM);
        if(type != null) sb.append(this.type.getPlaceholder());
        return sb.toString().toUpperCase();
    }
}
