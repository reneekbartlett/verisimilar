package com.reneekbartlett.verisimilar.core.generator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reneekbartlett.verisimilar.core.TestUtils;
import com.reneekbartlett.verisimilar.core.model.FilterOperator;
import com.reneekbartlett.verisimilar.core.model.GenderIdentity;
import com.reneekbartlett.verisimilar.core.model.PersonRecord;
import com.reneekbartlett.verisimilar.core.model.TemplateField;
import com.reneekbartlett.verisimilar.core.selector.filter.SelectionFilter;

public class AsyncPersonGeneratorTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPersonGeneratorTests.class);

    //private final Executor executor;

    public AsyncPersonGeneratorTests() {
        //this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Test
    public void GeneratePersonAsync_Test() {
        AsyncPersonGenerator generator = new AsyncPersonGenerator(TestUtils.getDatasetSelectionEngineRegistry());
        PersonRecord person = generator.generate();
        LOGGER.debug("Generated person: {}", person);
        Assertions.assertThat(person).isNotNull();
    }

    // TODO:  Fix.
    @Test
    public void GeneratePersonAsync_Test_NameFilter() {
        AsyncPersonGenerator asyncGenerator = new AsyncPersonGenerator(TestUtils.getDatasetSelectionEngineRegistry());

        String lastName = "BARTLETT";
        String city = "SHREWSBURY";

        SelectionFilter filter = SelectionFilter.builder()
                .lastName(lastName)
                //.startsWith("T", TemplateField.FIRST_NAME)
                .addFilter("T", TemplateField.FIRST_NAME, FilterOperator.STARTS_WITH)
                .city("SHREWSBURY")
                .build();
        PersonRecord person = asyncGenerator.generate(filter);
        LOGGER.debug("Generated person: {}", person);

        Assertions.assertThat(person).isNotNull();
        Assertions.assertThat(person.firstName().toUpperCase()).startsWith("T");
        Assertions.assertThat(person.lastName().toUpperCase()).isEqualTo(lastName);
        Assertions.assertThat(person.city().toUpperCase()).isEqualTo(city);
    }

    @Test
    public void GeneratePersonAsync_Test_PostalFilters() {
        AsyncPersonGenerator asyncGenerator = new AsyncPersonGenerator(TestUtils.getDatasetSelectionEngineRegistry());

        GenderIdentity gender = GenderIdentity.MALE;
        String firstName = "BOB";
        String lastNameChars = "BR";
        String streetName = "MAIN";
        String domain = "gmail.com";

        SelectionFilter filter = SelectionFilter.builder()
                .gender(gender)
                .firstName(firstName)
                //.startsWith(lastNameChars, TemplateField.LAST_NAME)
                .addFilter(lastNameChars, TemplateField.LAST_NAME, FilterOperator.STARTS_WITH)
                .streetName(streetName)
                .domain(domain)
                .build();
        PersonRecord person = asyncGenerator.generate(filter);
        LOGGER.debug("Generated person: {}", person);

        Assertions.assertThat(person.firstName()).isEqualToIgnoringCase(firstName);
        Assertions.assertThat(person.lastName()).startsWithIgnoringCase(lastNameChars);

        Assertions.assertThat(person.emailAddress().domain()).isEqualToIgnoringCase(domain);
    }
}
