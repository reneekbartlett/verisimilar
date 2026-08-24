package com.reneekbartlett.verisimilar.core.selector.engine;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.datasets.key.UsernameDatasetKey;
import com.reneekbartlett.verisimilar.core.datasets.resolver.UsernameDatasetResolver;
import com.reneekbartlett.verisimilar.core.datasets.resolver.registry.DatasetResolverRegistry;
import com.reneekbartlett.verisimilar.core.model.DomainType;
import com.reneekbartlett.verisimilar.core.model.FilterOperator;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;
import com.reneekbartlett.verisimilar.core.templates.loader.TemplateRegistryLoader;
import com.reneekbartlett.verisimilar.core.templates.resolver.UsernameTemplatesResolver;

/***
 * 
 */
public class UsernameSelectionEngineTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameSelectionEngineTests.class);

    //private DatasetResolverRegistry resolvers;
    //private UsernameDatasetResolver usernameDatasetResolver;
    //private UsernameSelectionEngine usernameSelector1;
    //private UsernameSelectionEngine usernameSelector2;

    //private UsernameTemplatesResolver templatesResolver = new UsernameTemplatesResolver(new TemplateRegistryLoader());

    public UsernameSelectionEngineTests() {
        //this.usernameSelector1 = new UsernameSelectionEngine(resolvers, TestUtils.UNIFORM_RANDOM, templateRegistry);
    }

    @Test
    public void GenerateUsername_Random() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameDatasetResolver usernameDatasetResolver = resolvers.username();

        //TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        //TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(usernameDatasetResolver);

        //select(UsernameDatasetKey key, SelectionFilter filter)
        UsernameDatasetKey key;
        SelectionFilter usernameFilter = SelectionFilter.builder()
                .domain("yahoo.com")
                .domainType(DomainType.B2C)
                .addFilter("G", TemplateField.USERNAME, FilterOperator.STARTS_WITH)
                .build();

        String username2 = usernameSelector.select(usernameFilter);
        LOGGER.debug("username2={}", username2);
        Assertions.assertThat(username2).isNotNull()
            .startsWithIgnoringCase("G");
    }

    @Test
    public void GenerateUsername_MatchesDomainRules() {
        DatasetResolverRegistry resolvers = TestUtils.getEmailAddressDatasetResolverRegistry();
        UsernameDatasetResolver usernameDatasetResolver = resolvers.username();
        //TemplateRegistryLoader templateLoader = new TemplateRegistryLoader();
        //TemplateRegistry templateRegistry = templateLoader.loadFromClasspath("templates/username-templates.yaml");

        UsernameSelectionEngine usernameSelector = new UsernameSelectionEngine(usernameDatasetResolver);

        //select(UsernameDatasetKey key, SelectionFilter filter)
        //UsernameDatasetKey key;
        SelectionFilter usernameFilter = SelectionFilter.builder()
                .domain("yahoo.com")
                .domainType(DomainType.B2C)
                .addFilter("G", TemplateField.USERNAME, FilterOperator.STARTS_WITH)
                .build();

        String username = usernameSelector.select(usernameFilter);
        LOGGER.debug("username={}", username);
        Assertions.assertThat(username).isNotNull()
            .startsWithIgnoringCase("G");
    }

    private void chk() {
        
        //TemplateParameters parameters = getTemplateParameters(filter, usernameKeyword1, usernameKeyword2);
        //Map<String, Object> allTemplateParams = new HashMap<>(parameters.resolved());
        //UsernameTemplatesResult templatesResult = templatesResolver.loadForFields(parameters.populatedFields());

        //TemplateSet templateSet = templatesResult.getTemplates();
        //if(templateSet.templates().size() == 0) {
        //    LOGGER.warn("No Templates...");
        //}

        // Pick a template
        // TODO:  Backup?  "${KEYWORD}${NUM1000}"
        //UniformSelectorImpl<String> templateSelector = new UniformSelectorImpl<>(templateSet.toList(), null);
        //String randomTemplate = templateSelector.select();
        //String usernameFromTemplate = applyTemplate(randomTemplate, usernameKeyword1, allTemplateParams);
        //LOGGER.debug("randomTemplate:{}; usernameFromTemplate:{}", randomTemplate, usernameFromTemplate);
        //LOGGER.trace("templatesResult:{}", templatesResult.toString());

        //return usernameFromTemplate;
    }

}
