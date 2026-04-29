package com.reneekbartlett.verisimilar.core.generator;

import java.util.Map;

import com.reneekbartlett.verisimilar.core.generator.api.AbstractValueGenerator;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;

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

    @Override
    protected EmailAddressRecord generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generateEmailAddress(ctx, filter);
    }

    private EmailAddressRecord generateEmailAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        if(filter.domainType().isEmpty()) {
            DomainType domainType = getRandomDomainType();
            filter = filter.toBuilder().domainType(domainType).build();
        }

        // First get Domain
        String domain = generateDomain(ctx, filter);

        // Then get Username, with domain passed in filter
        SelectionFilter usernameFilter = filter.toBuilder().domain(domain).build();
        String username = generateUsername(ctx, usernameFilter);

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

    private DomainType getRandomDomainType() {
        Map<DomainType, Double> domainTypesMap = DomainType.defaultMap();
        RandomSelector<DomainType> selector = new WeightedSelectorImpl<>(domainTypesMap);
        return selector.select();
    }

}
