package com.reneekbartlett.verisimilar.core.model;

// TODO:  KeywordType, Keyword1, Keyword2
public record UsernameRecord (String username, UsernameType usernameType, DomainRecord domainRecord){

    public UsernameRecord(String username, UsernameType usernameType) {
        this(username, usernameType, new DomainRecord(null,null));
    }

    @Override
    public String toString() {
        final String VALUE_DELIM = " ";
        StringBuilder sb = new StringBuilder(0);
        sb.append(this.username).append(VALUE_DELIM);
        if(usernameType != null) sb.append(this.usernameType.getLabel());
        if(domainRecord != null) sb.append(this.domainRecord.toString());
        return sb.toString().toUpperCase();
    }
}
