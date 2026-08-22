package com.reneekbartlett.verisimilar.core.model;

import java.util.Set;

import ch.qos.logback.core.util.StringUtil;

public record EmailAddressRecord(String username, String domain, DomainType type) implements CombinationResultField {

    public EmailAddressRecord(String username, String domain) {
        // TODO:  Use default DomainType? Or set to Unknown?
        this(username, domain, null);
    }

    public EmailAddressRecord(String username, DomainRecord domainRecord) {
        this(username, domainRecord.domain(), domainRecord.domainType());
    }

    @Override
    public DomainType type() {
        return type == null ? DomainType.B2C : type;
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

    @Override
    public String toValueString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<TemplateField> getFields() {
        // TODO Auto-generated method stub
        return null;
    }
}
