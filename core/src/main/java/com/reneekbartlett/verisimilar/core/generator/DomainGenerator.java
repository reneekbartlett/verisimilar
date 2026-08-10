//package com.reneekbartlett.verisimilar.core.generator;
//
//import java.util.List;
//
//import com.reneekbartlett.verisimilar.core.datasets.key.DomainDatasetKey;
//import com.reneekbartlett.verisimilar.core.model.DomainType;
//import com.reneekbartlett.verisimilar.core.model.TemplateField;
//import com.reneekbartlett.verisimilar.core.pipeline.DatasetResolutionContext;
//import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
//import com.reneekbartlett.verisimilar.core.selector.engine.DomainSelectionEngine;
//import com.reneekbartlett.verisimilar.core.selector.engine.registry.DatasetSelectionEngineRegistry;
//
///***
// * 
// */
//public class DomainGenerator extends AbstractStringGenerator {
//
//    private final DomainSelectionEngine domainSelector;
//    private final List<TemplateField> filterFields;
//
//    public DomainGenerator(DomainSelectionEngine domainSelector) {
//        this.domainSelector = domainSelector;
//        this.filterFields = List.of(TemplateField.DOMAIN, TemplateField.DOMAIN_TYPE);
//    }
//
//    public DomainGenerator(DatasetSelectionEngineRegistry selectors) {
//        this(selectors.domain());
//    }
//
//    @Override
//    protected String generateString(DatasetResolutionContext ctx, SelectionFilter filter) {
//        DomainDatasetKey key = DomainDatasetKey.fromContext(ctx);
//
//        // First get Domain if specified
//        DomainType domainType = filter.domainType().orElseGet(() -> generateDomainType(ctx, filter));
//
//        return generateDomain(key, filter);
//    }
//
//    @Override
//    public List<TemplateField> filterFields() {
//        return this.filterFields;
//    }
//
//    private DomainType generateDomainType(DatasetResolutionContext ctx, SelectionFilter filter) {
//        // TODO:  Pick Random
//        return DomainType.B2C;
//    }
//
//    private String generateDomain(DomainDatasetKey key, SelectionFilter filter) {
//        String valueFilter = filter.equalToMap().get(TemplateField.DOMAIN);
//        if (valueFilter != null) {
//            return valueFilter;
//        }
//
//        // TODO: Pick random type?
//        
//        return domainSelector.select(key, filter);
//    }
//}
