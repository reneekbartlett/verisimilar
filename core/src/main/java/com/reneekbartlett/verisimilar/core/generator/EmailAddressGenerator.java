package com.reneekbartlett.verisimilar.core.generator;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

public class EmailAddressGenerator extends AbstractValueGenerator<EmailAddressRecord> {

    private final UsernameGenerator usernameGenerator;
    private final DomainGenerator domainGenerator;

    public EmailAddressGenerator(
            UsernameSelectionEngine usernameSelector, 
            DomainSelectionEngine domainSelector,
            KeywordSelectionEngine keywordSelector
    ) {
        this.usernameGenerator = new UsernameGenerator(usernameSelector);
        this.domainGenerator = new DomainGenerator(domainSelector);
    }

    public EmailAddressGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.username(), selectors.domain(), selectors.keyword());
    }

    @Override
    protected EmailAddressRecord generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generateEmailAddress(ctx, filter);
    }

    private EmailAddressRecord generateEmailAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        // First get Domain
        String domain = filter.domain().orElseGet(() -> generateDomain(ctx, filter));

        // Then get Username, with domain passed in filter
        SelectionFilter usernameFilter = filter.toBuilder().domain(domain).build();
        String username = filter.username().orElseGet(() -> generateUsername(ctx, usernameFilter));

        // TODO:  Post process.  Check handle/domain syntax.
        return new EmailAddressRecord(username.toUpperCase(), domain);
    }

    private String generateDomain(DatasetResolutionContext ctx, SelectionFilter filter) {
        return domainGenerator.generate(ctx, filter);
    }

    private String generateUsername(DatasetResolutionContext ctx, SelectionFilter filter) {
        return usernameGenerator.generate(filter);
    }

    @Override
    protected Class<EmailAddressRecord> valueType() {
        return EmailAddressRecord.class;
    }
}
