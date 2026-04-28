package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

public class EmailAddressGenerator extends AbstractValueGenerator<EmailAddressRecord> {

    private final UsernameGenerator usernameGenerator;
    private final DomainGenerator domainGenerator;

    public EmailAddressGenerator(UsernameSelectionEngine usernameSelector, 
            DomainSelectionEngine domainSelector,
            KeywordSelectionEngine keywordSelector) {
        this.usernameGenerator = new UsernameGenerator(usernameSelector);
        this.domainGenerator = new DomainGenerator(domainSelector);
    }

    @Override
    protected EmailAddressRecord generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generateEmailAddress(ctx, filter);
    }

    @Override
    protected Class<EmailAddressRecord> valueType() {
        return EmailAddressRecord.class;
    }

    //TODO
    private EmailAddressRecord generateEmailAddress(DatasetResolutionContext ctx, SelectionFilter filter) {

        String domain = getDomain(ctx, filter);

        // TODO:  set user filter from ^^
        SelectionFilter.Builder usernameFilterBuilder = SelectionFilter.builder().domain(domain);
        if(filter.firstName().isPresent()) usernameFilterBuilder.firstName(filter.firstName().get());
        if(filter.middleName().isPresent()) usernameFilterBuilder.middleName(filter.middleName().get());
        if(filter.lastName().isPresent()) usernameFilterBuilder.lastName(filter.lastName().get());
        if(filter.birthday().isPresent()) usernameFilterBuilder.birthday(filter.birthday().get());
        String username = getUsername(ctx, usernameFilterBuilder.build());

        return new EmailAddressRecord(username, domain);
    }

    private String getDomain(DatasetResolutionContext ctx, SelectionFilter filter) {
        return domainGenerator.generate(ctx, filter);
    }

    private String getUsername(DatasetResolutionContext ctx, SelectionFilter filter) {
        return usernameGenerator.generate(filter).toUpperCase();
    }

}
