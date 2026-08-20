package com.reneekbartlett.verisimilar.core.generator;

import java.util.ArrayList;
import java.util.List;

import com.reneekbartlett.verisimilar.core.model.DomainRecord;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.EmailAddressRecord;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.selector.RandomSelector;
import com.reneekbartlett.verisimilar.core.selector.WeightedSelectorImpl;
import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.KeywordSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.UsernameSelectionEngine;
import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;

/***
 * Composite generator, using UsernameGenerator + DomainGenerator
 */
public class EmailAddressGenerator extends AbstractValueGenerator<EmailAddressRecord> {

    private final UsernameGenerator usernameGenerator;
    private final DomainRecordGenerator domainRecordGenerator;

    public EmailAddressGenerator(
            UsernameSelectionEngine usernameSelector, 
            DomainSelectionEngine domainSelector,
            KeywordSelectionEngine keywordSelector
    ) {
        this.usernameGenerator = new UsernameGenerator(usernameSelector);
        this.domainRecordGenerator = new DomainRecordGenerator(domainSelector);
        // TODO:  Use KeywordSelectionEngine?
    }

    public EmailAddressGenerator(DatasetSelectionEngineRegistry selectors) {
        this(selectors.username(), selectors.domain(), selectors.keyword());
    }

    @Override
    protected EmailAddressRecord generateValue(DatasetResolutionContext ctx, SelectionFilter filter) {
        return generateEmailAddress(ctx, filter);
    }

    private EmailAddressRecord generateEmailAddress(DatasetResolutionContext ctx, SelectionFilter filter) {
        // First get DomainRecord if specified
        DomainRecord domainRecord = filter.domainRecord().orElseGet(() -> generateDomainRecord(ctx, filter));

        if(domainRecord.domainType() == null) {
            DomainType domainType = generateDomainType(filter);
            domainRecord = new DomainRecord(domainRecord.domain(), domainType);
        }

        // Then get Username, with domain and domainType passed in filter
        SelectionFilter usernameFilter = SelectionFilter.toBuilder(filter)
                .domain(domainRecord.domain())
                .domainType(domainRecord.domainType())
                .build();
        String username = filter.username().orElseGet(() -> generateUsername(ctx, usernameFilter));

        // TODO:  Post process.  Check handle/domain syntax.
        return new EmailAddressRecord(username.toUpperCase(), domainRecord);
    }

    /***
     * Use DomainGenerator to generate new domain, using filters passed to EmailAddressGenerator
     * > DomainType
     * @param ctx
     * @param filter
     * @return
     */
    private DomainRecord generateDomainRecord(DatasetResolutionContext ctx, SelectionFilter filter) {
        return domainRecordGenerator.generate(ctx, filter);
    }

    private DomainType generateDomainType(SelectionFilter filter) {
        return filter.domainType().orElseGet(() -> {
            RandomSelector<DomainType> domainTypeSelector = new WeightedSelectorImpl<>(DomainType.defaultMap(), TemplateField.DOMAIN_TYPE);
            return domainTypeSelector.select();
        });
    }

    /***
     * Use UsenameGenerator to generate new username, 
     * using filters passed to EmailAddressGenerator and result from DomainGenerator 
     * since Domain/DomainType will impact username requirements/spec.
     * 
     */
    private String generateUsername(DatasetResolutionContext ctx, SelectionFilter filter) {
        return usernameGenerator.generate(filter);
    }

    @Override
    protected EmailAddressRecord postProcess(EmailAddressRecord emailAddressRecord) {
        // TODO:  Validate Handle
        return emailAddressRecord;
    }

    @Override
    protected Class<EmailAddressRecord> valueType() {
        return EmailAddressRecord.class;
    }

    @Override
    public List<TemplateField> filterFields(){
        List<TemplateField> filterFields = new ArrayList<>();
        filterFields.addAll(usernameGenerator.filterFields());
        filterFields.addAll(domainRecordGenerator.filterFields());
        return filterFields;
    }
}
